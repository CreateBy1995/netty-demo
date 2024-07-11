```java
 import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoop;

private ChannelFuture doBind(final SocketAddress localAddress) {
    final ChannelFuture regFuture = initAndRegister(); // 初始化并注册Channel
    final Channel channel = regFuture.channel(); // 获取Channel

    if (regFuture.cause() != null) { // 检查注册是否失败
        return regFuture;
    }

    if (regFuture.isDone()) { // 注册已完成
        ChannelPromise promise = channel.newPromise(); // 创建新的ChannelPromise
        // 异步执行绑定任务，由NioEventLoop处理
        doBind0(regFuture, channel, localAddress, promise); // 执行实际绑定操作
        return promise;
    } else { // 注册未完成
        final PendingRegistrationPromise promise = new PendingRegistrationPromise(channel);
        regFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                Throwable cause = future.cause();
                if (cause != null) {
                    promise.setFailure(cause); // 设置失败状态
                } else {
                    promise.registered();
                    doBind0(regFuture, channel, localAddress, promise); // 执行实际绑定操作
                }
            }
        });
        return promise;
    }
}


final ChannelFuture initAndRegister() {
    Channel channel = null;
    try {
        // 创建一个还未绑定端口的serverChannel，同时设置为非阻塞模式
        channel = channelFactory.newChannel();
        // 设置用户配置的处理器以及一些操作
        init(channel);
    } catch (Throwable t) {
        if (channel != null) {
            // channel can be null if newChannel crashed (eg SocketException("too many open files"))
            channel.unsafe().closeForcibly();
            // as the Channel is not registered yet we need to force the usage of the GlobalEventExecutor
            return new DefaultChannelPromise(channel, GlobalEventExecutor.INSTANCE).setFailure(t);
        }
        // as the Channel is not registered yet we need to force the usage of the GlobalEventExecutor
        return new DefaultChannelPromise(new FailedChannel(), GlobalEventExecutor.INSTANCE).setFailure(t);
    }
    // 执行注册，注册任务是在异步执行的，也就是由NioEventLoop处理
    ChannelFuture regFuture = config().group().register(channel);
    if (regFuture.cause() != null) {
        if (channel.isRegistered()) {
            channel.close();
        } else {
            channel.unsafe().closeForcibly();
        }
    }

    // If we are here and the promise is not failed, it's one of the following cases:
    // 1) If we attempted registration from the event loop, the registration has been completed at this point.
    //    i.e. It's safe to attempt bind() or connect() now because the channel has been registered.
    // 2) If we attempted registration from the other thread, the registration request has been successfully
    //    added to the event loop's task queue for later execution.
    //    i.e. It's safe to attempt bind() or connect() now:
    //         because bind() or connect() will be executed *after* the scheduled registration task is executed
    //         because register(), bind(), and connect() are all bound to the same thread.

    return regFuture;
}


protected void run() {
    int selectCnt = 0;
    for (; ; ) {
        try {
            int strategy;
            try {
                // 1.默认的策略
                // a.不存在任务时，使用SELECT策略
                // b.存在任务时，根据就绪的IO事件数(执行selectNow方法)选择相应的策略
                strategy = selectStrategy.calculateStrategy(selectNowSupplier, hasTasks());
                switch (strategy) {
                    case SelectStrategy.CONTINUE:
                        continue;

                    case SelectStrategy.BUSY_WAIT:
                        // fall-through to SELECT since the busy-wait is not supported with NIO

                    case SelectStrategy.SELECT:
                        // 存在定时任务
                        // a.时间未到，则阻塞（ 调用select(timeout)方法 ）到定时任务执行的时间
                        // b.时间到了，则不阻塞（ 调用selectNow方法 ）
                        // 不存在定时任务的情况下一直（ 调用select()方法 ）阻塞直到有IO事件就绪
                        long curDeadlineNanos = nextScheduledTaskDeadlineNanos();
                        if (curDeadlineNanos == -1L) {
                            curDeadlineNanos = NONE; // nothing on the calendar
                        }
                        nextWakeupNanos.set(curDeadlineNanos);
                        try {
                            if (!hasTasks()) {
                                strategy = select(curDeadlineNanos);
                            }
                        } finally {
                            // This update is just to help block unnecessary selector wakeups
                            // so use of lazySet is ok (no race condition)
                            nextWakeupNanos.lazySet(AWAKE);
                        }
                        // fall through
                    default:
                }
            } catch (IOException e) {
                // If we receive an IOException here its because the Selector is messed up. Let's rebuild
                // the selector and retry. https://github.com/netty/netty/issues/8566
                rebuildSelector0();
                selectCnt = 0;
                handleLoopException(e);
                continue;
            }

            selectCnt++;
            cancelledKeys = 0;
            needsToSelectAgain = false;
            final int ioRatio = this.ioRatio;
            boolean ranTasks;
            // ioRatio表示IO任务和非IO任务处理的事件占比，默认是50%，也就是各用50的时间去处理。
            // 但是ioRatio=100的时候会将所有的时间用于处理 I/O 操作，而不会分配时间执行非 I/O 任务（如定时任务和用户提交的任务）。
            // 这意味着 EventLoop 将优先处理 I/O 事件，而只有在没有 I/O 事件需要处理时才会执行任务
            if (ioRatio == 100) {
                try {
                    // 优先处理IO事件
                    if (strategy > 0) {
                        processSelectedKeys();
                    }
                } finally {
                    // 处理完IO事件在处理所有非IO任务
                    // Ensure we always run tasks.
                    ranTasks = runAllTasks();
                }
            } else if (strategy > 0) {
                // 按比例分别处理IO任务和其他任务
                final long ioStartTime = System.nanoTime();
                try {
                    processSelectedKeys();
                } finally {
                    // Ensure we always run tasks.
                    final long ioTime = System.nanoTime() - ioStartTime;
                    // 在给定的时间内尽可能多地执行非IO任务，而这个时间就是根据ioRatio计算出来的。
                    ranTasks = runAllTasks(ioTime * (100 - ioRatio) / ioRatio);
                }
            } else {
                // 每次执行最少的非IO任务
                ranTasks = runAllTasks(0); // This will run the minimum number of tasks
            }

            if (ranTasks || strategy > 0) {
                if (selectCnt > MIN_PREMATURE_SELECTOR_RETURNS && logger.isDebugEnabled()) {
                    logger.debug("Selector.select() returned prematurely {} times in a row for Selector {}.",
                            selectCnt - 1, selector);
                }
                selectCnt = 0;
            } else if (unexpectedSelectorWakeup(selectCnt)) { // Unexpected wakeup (unusual case)
                selectCnt = 0;
            }
        } catch (CancelledKeyException e) {
            // Harmless exception - log anyway
            if (logger.isDebugEnabled()) {
                logger.debug(CancelledKeyException.class.getSimpleName() + " raised by a Selector {} - JDK bug?",
                        selector, e);
            }
        } catch (Error e) {
            throw e;
        } catch (Throwable t) {
            handleLoopException(t);
        } finally {
            // Always handle shutdown even if the loop processing threw an exception.
            try {
                if (isShuttingDown()) {
                    closeAll();
                    if (confirmShutdown()) {
                        return;
                    }
                }
            } catch (Error e) {
                throw e;
            } catch (Throwable t) {
                handleLoopException(t);
            }
        }
    }
}


protected boolean runAllTasks(long timeoutNanos) {
    // 将到达执行时间的定时任务拉到当前线程的任务队列 队列头?
    fetchFromScheduledTaskQueue();
    // 从任务队列拉取任务
    Runnable task = pollTask();
    if (task == null) {
        afterRunningAllTasks();
        return false;
    }
    // 获取超时时长
    final long deadline = timeoutNanos > 0 ? getCurrentTimeNanos() + timeoutNanos : 0;
    long runTasks = 0;
    long lastExecutionTime;
    for (; ; ) {
        // 任务执行
        safeExecute(task);

        runTasks++;
        // 检测是否超时，每64个任务检测一次，个人理解提高了系统的吞吐量，但相应的可能也增加了延迟。
        // Check timeout every 64 tasks because nanoTime() is relatively expensive.
        if ((runTasks & 0x3F) == 0) {
            lastExecutionTime = getCurrentTimeNanos();
            if (lastExecutionTime >= deadline) {
                break;
            }
        }

        task = pollTask();
        if (task == null) {
            lastExecutionTime = getCurrentTimeNanos();
            break;
        }
    }

    afterRunningAllTasks();
    this.lastExecutionTime = lastExecutionTime;
    return true;
}

private AbstractChannelHandlerContext findContextInbound(int mask) {
    AbstractChannelHandlerContext ctx = this;
    EventExecutor currentExecutor = executor();
    do {
        ctx = ctx.next;
        // 判断当前的handler是否需要跳过，比如当前事件如果是一个入站事件，而handler又是一个出站处理器
        // 则跳过该处理器继续寻找下一个。
    } while (skipContext(ctx, currentExecutor, mask, MASK_ONLY_INBOUND));
    return ctx;
}
/***
 * ServerBootstrapAcceptor#channelRead处理连接accept事件，
 */
/***
 * AbstractNioByteChannel#read 读事件处理
 */
/***
 * 1.管道工作机制
 * 2.事件循环组工作机制
 * 3.内存分配机制
 */


```
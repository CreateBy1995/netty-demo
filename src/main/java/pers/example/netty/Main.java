package pers.example.netty;

import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.ThreadPerTaskExecutor;
import lombok.extern.slf4j.Slf4j;
import pers.example.netty.client.EchoClient;
import pers.example.netty.server.EchoServer;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Main {
    public static void main(String[] args) throws InterruptedException {
        test0001();
    }

    public static void test0000() throws InterruptedException {
        int port = 8070;
        new Thread(() -> {
            EchoServer echoServer = new EchoServer();
            try {
                echoServer.run(port);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
        TimeUnit.SECONDS.sleep(1);
        EchoClient echoClient = new EchoClient();
        echoClient.connect(port);
    }

    /**
     * EventLoopGroup使用
     * EventLoopGroup包含多个EventLoop。
     * 每个EventLoop都有一个独立的线程来处理事件，一个EventLoop可以处理多个Channel的事件，但一个Channel只能被一个EventLoop处理。
     * 当一个新的Channel注册到EventLoopGroup时，它会被分配给其中的一个EventLoop。
     */
    public static void test0001() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        EventLoop loop = group.next();
        Runnable runnable = () -> {
            log.info("loop execute");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        loop.execute(runnable);
        loop.execute(runnable);
        log.info("main sleep");
        TimeUnit.SECONDS.sleep(5);
    }


    /**
     * ThreadPerTaskExecutor 测试
     * ThreadPerTaskExecutor非常特殊，没有工作队列，每个任务都是新创建一个线程去执行
     *
     * @throws InterruptedException
     */
    public static void test0002() throws InterruptedException {
        Executor executor = new ThreadPerTaskExecutor(new DefaultThreadFactory(EchoClient.class.getName()));
        Runnable runnable = () -> {
            log.info("test0002 ...");
        };
        executor.execute(runnable);
        executor.execute(runnable);
        TimeUnit.SECONDS.sleep(2);
    }
}

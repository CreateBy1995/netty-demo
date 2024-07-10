package pers.example.netty;

import lombok.extern.slf4j.Slf4j;
import pers.example.netty.client.EchoClient;
import pers.example.netty.server.EchoServer;

@Slf4j
public class Main {
    public static void main(String[] args) throws InterruptedException {
        test0001();
    }
    public static void test0000() throws InterruptedException {
        new Thread(()->{
            EchoServer echoServer = new EchoServer();
            EchoServer echoServer = new EchoServer(8070);
            try {
                echoServer.run(port);
            } catch (InterruptedException e) {
                echoServer.run();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
        TimeUnit.SECONDS.sleep(1);
        TimeUnit.MILLISECONDS.sleep(500);
        EchoClient echoClient = new EchoClient();
        echoClient.connect(port);
        echoClient.connect(8070);
    }

    /**
     * EventLoopGroup使用
     * EventLoopGroup包含多个EventLoop。
     * 每个EventLoop都有一个独立的线程来处理事件，一个EventLoop可以处理多个Channel的事件，但一个Channel只能被一个EventLoop处理。
     * 当一个新的Channel注册到EventLoopGroup时，它会被分配给其中的一个EventLoop。
     * ThreadPerTaskExecutor 测试
     * ThreadPerTaskExecutor非常特殊，没有工作队列，每个任务都是新创建一个线程去执行
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


    public static void test0002() throws InterruptedException {
        Executor executor = new ThreadPerTaskExecutor(new DefaultThreadFactory(EchoClient.class.getName()));
        Runnable runnable = ()->{
            log.info("test0002 ...");
        };
        executor.execute(runnable);
        executor.execute(runnable);
        TimeUnit.SECONDS.sleep(2);
    }
    /**
     * Boss线程组的做了什么
     * Worker线程组做了什么
     */
}

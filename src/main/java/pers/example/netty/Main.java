package pers.example.netty;

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

    public static void test0001() throws InterruptedException {
        int port = 8070;
        new Thread(()->{
            EchoServer echoServer = new EchoServer();
            try {
                echoServer.run(port);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        TimeUnit.SECONDS.sleep(1);
        EchoClient echoClient = new EchoClient();
        echoClient.connect(port);
    }

    /**
     * ThreadPerTaskExecutor 测试
     * ThreadPerTaskExecutor非常特殊，没有工作队列，每个任务都是新创建一个线程去执行
     */

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

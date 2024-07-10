package pers.example.netty;

import io.netty.channel.Channel;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;
import pers.example.netty.client.EchoClient;
import pers.example.netty.server.EchoServer;

import java.sql.Time;
import java.util.concurrent.*;

/**
 * @Author: dongcx
 * @CreateTime: 2024-07-09
 * @Description:
 */
@Slf4j
public class Main {
    public static void main(String[] args) throws Exception {
    test0001();

    }
    public static void test0000() throws InterruptedException {
        new Thread(()->{
            EchoServer echoServer = new EchoServer(8070);
            try {
                echoServer.run();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
        TimeUnit.MILLISECONDS.sleep(500);
        EchoClient echoClient = new EchoClient();
        echoClient.connect(8070);
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

}

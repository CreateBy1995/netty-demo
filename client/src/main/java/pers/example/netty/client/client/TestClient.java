package pers.example.netty.client.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import pers.example.netty.client.handler.EchoClientHandler;
import pers.example.netty.client.handler.TestClientHandler;

@Slf4j
public class TestClient {
    private final static String HOST = "localhost";

    public ChannelFuture connect(int port) throws InterruptedException {
        EventLoopGroup clientGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(clientGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new TestClientHandler());
                    }
                });
        ChannelFuture channelFuture = bootstrap.connect(HOST, port).sync();
        log.info("echo client connect with port: {}", port);
       return channelFuture;
    }
}

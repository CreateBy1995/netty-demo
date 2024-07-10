package pers.example.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import pers.example.netty.handler.EchoClientHandler;

import java.util.concurrent.TimeUnit;

/**
 * @Author: dongcx
 * @CreateTime: 2024-07-09
 * @Description:
 */
public class EchoClient {
    private final static String HOST = "localhost";

    public void connect(int port) throws InterruptedException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(new EchoClientHandler());
                }
            });

            ChannelFuture f = b.connect(HOST, port).sync();
            f.channel().writeAndFlush("client init 11");
            TimeUnit.MILLISECONDS.sleep(500);
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}

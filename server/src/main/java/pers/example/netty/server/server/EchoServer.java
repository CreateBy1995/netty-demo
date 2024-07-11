package pers.example.netty.server.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import pers.example.netty.server.handler.EchoServerChildInitializer;

@Slf4j
public class EchoServer {

    public void run(int port) throws InterruptedException {
        // 参数表示需要创建的EventLoop数量
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup(5);

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        // 配置设置
        serverBootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new EchoServerChildInitializer());
        // 绑定端口并启动
        ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
        log.info("echo server run with port: {}", port);
        channelFuture.channel().closeFuture().sync();
        log.info("echo server shutdown");
    }
}

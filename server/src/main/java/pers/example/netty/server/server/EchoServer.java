package pers.example.netty.server.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import pers.example.netty.server.handler.EchoServerHandler;

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
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        socketChannel.pipeline().addLast(new EchoServerHandler());
                    }
                });
        // 绑定端口并启动
        ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
        log.info("echo server run with port: {}", port);
//        new Thread(() -> {
//            try {
//                TimeUnit.SECONDS.sleep(5);
//                log.info("server channel close ...");
//                channelFuture.channel().close();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }).start();
        channelFuture.channel().closeFuture().sync();
        log.info("echo server shutdown");
    }
}

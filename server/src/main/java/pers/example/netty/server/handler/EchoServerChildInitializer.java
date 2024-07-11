package pers.example.netty.server.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @Author: dongcx
 * @CreateTime: 2024-07-10
 * @Description:
 */
public class EchoServerChildInitializer extends ChannelInitializer<SocketChannel> {
    /**
     * channel注册到eventLoop时会回调这个方法。
     * @param
     */
    @Override
    protected void initChannel(SocketChannel ch){
        ch.pipeline().addLast(new EchoServerHandler());
    }
}

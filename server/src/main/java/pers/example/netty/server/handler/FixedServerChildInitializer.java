package pers.example.netty.server.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;

/**
 * @Author: dongcx
 * @CreateTime: 2024-07-10
 * @Description:
 */
public class FixedServerChildInitializer extends ChannelInitializer<SocketChannel> {
    /**
     * FixedLengthFrameDecoder是ByteToMessageDecoder的实现类，用于解析定长数据。
     * ByteToMessageDecoder#channelRead 粘包拆包的抽象处理在这个方法，
     * 核心思想就是每当接受到的数据时，判断其是否为一个完整的包，如果是则将该包传递给下一个处理器（比如用户的业务处理器），
     * 否则先将数据缓存起来，等待下一个数据包
     * 至于怎么判定是一个完整的包就看具体子类的实现。比如通过定长、分隔符等、
     */
    @Override
    protected void initChannel(SocketChannel ch){
        ch.pipeline().addLast(new FixedLengthFrameDecoder(20),
                new FixedServerHandler());
    }
}

package pers.example.netty.server.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;

/**
 * @Author: dongcx
 * @CreateTime: 2024-07-10
 * @Description:
 */
public class InboundInitializer extends ChannelInitializer<EmbeddedChannel> {
    @Override
    protected void initChannel(EmbeddedChannel ch){
        // ChannelPipeline中介绍了处理器的执行顺序
        // 如果是入站的请求的话是从头结点开始执行的，也就是A -> B
        // 如果是出站的请求是从尾节点开始执行的 也就是 B -> A
        ch.pipeline().addLast(new InboundHandlerA());
        ch.pipeline().addLast(new InboundHandlerB());
        ch.pipeline().addLast(new OutboundHandlerA());
        ch.pipeline().addLast(new OutboundHandlerB());
        ch.pipeline().addLast(new DuplexHandlerA());
    }
}

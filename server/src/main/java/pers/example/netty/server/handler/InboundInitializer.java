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
    protected void initChannel(EmbeddedChannel ch) throws Exception {
        ch.pipeline().addLast(new InboundHandlerA());
        ch.pipeline().addLast(new InboundHandlerB());
        ch.pipeline().addLast(new OutboundHandlerA());
        ch.pipeline().addLast(new OutboundHandlerB());
        ch.pipeline().addLast(new DuplexHandlerA());
    }
}

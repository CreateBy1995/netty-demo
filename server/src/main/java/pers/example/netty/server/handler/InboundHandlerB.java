package pers.example.netty.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InboundHandlerB extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        log.info("inboundHandlerB channelRead, msg is :{}", byteBuf.toString(CharsetUtil.UTF_8));
        // 调用的是 ctx.fireChannelRead(msg)，也就是继续向下一个handler传递
        super.channelRead(ctx, byteBuf);
    }

}

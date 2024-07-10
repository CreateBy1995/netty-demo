package pers.example.netty.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

@Slf4j
public class InboundHandlerA extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        log.info("inboundHandlerA channelRead, msg is :{}", byteBuf.toString(CharsetUtil.UTF_8));
        // 调用的是 ctx.fireChannelRead(msg)，也就是继续向下一个handler传递
        super.channelRead(ctx, byteBuf);
    }

}

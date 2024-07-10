package pers.example.netty.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DuplexHandlerA extends ChannelDuplexHandler {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        log.info("duplexHandlerA write, msg is :{}", byteBuf.toString(CharsetUtil.UTF_8));
//        promise.addListener((GenericFutureListener) future -> {
//            log.info("outboundHandlerB done, future", future);
//        });
        // 继续向下一个handler传递
        super.write(ctx, byteBuf, promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        log.info("duplexHandlerA channelRead, msg is :{}", byteBuf.toString(CharsetUtil.UTF_8));
        // 调用的是 ctx.fireChannelRead(msg)，也就是继续向下一个handler传递
        super.channelRead(ctx, byteBuf);
    }

}

package pers.example.netty.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OutboundHandlerA extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        log.info("outboundHandlerA write, msg is :{}", byteBuf.toString(CharsetUtil.UTF_8));
//        promise.addListener((GenericFutureListener) future -> {
//            log.info("outboundHandlerA done, future", future);
//        });
        // 继续向下一个handler传递
        super.write(ctx, byteBuf, promise);
    }

}

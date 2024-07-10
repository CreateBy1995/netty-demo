package pers.example.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: dongcx
 * @CreateTime: 2024-07-09
 * @Description:
 */
@Slf4j
public class EchoClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;
        log.info("echoClientHandler channelRead, msg:{}", in.toString(CharsetUtil.UTF_8));
        ctx.write("client receive msg: " + in.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("echoClientHandler channelActive");
        ctx.writeAndFlush("client receive msg: " )
                .addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) {
                        log.info("echoClientHandler done");
                        ctx.close();
                    }
                });
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("echoClientHandler exceptionCaught, ctx={}", ctx, cause);
    }
}

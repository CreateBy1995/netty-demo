package pers.example.netty.client.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FixedClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("echo client ==> channelActive");
        gt(ctx);
//        super.channelActive(ctx);
    }
    private void lt(ChannelHandlerContext ctx){
        // 服务端定长20个字节
        // 单次不足20个字符
        ctx.writeAndFlush(Unpooled.copiedBuffer("12345678910", CharsetUtil.UTF_8));
        // 打个断点
//        TimeUnit.SECONDS.sleep(10000);
        ctx.writeAndFlush(Unpooled.copiedBuffer("abcdefghijk", CharsetUtil.UTF_8));
    }

    private void gt(ChannelHandlerContext ctx){
        // 服务端定长20个字节
        // 单次超过20个字符
        ctx.writeAndFlush(Unpooled.copiedBuffer("123456789101234567891012345678910", CharsetUtil.UTF_8));
        // 打个断点
//        TimeUnit.SECONDS.sleep(10000);
        ctx.writeAndFlush(Unpooled.copiedBuffer("abcdefghijksss", CharsetUtil.UTF_8));
    }
}

package pers.example.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import lombok.extern.slf4j.Slf4j;
import pers.example.netty.server.handler.InboundInitializer;
import pers.example.netty.server.server.EchoServer;

/**
 * @Author: dongcx
 * @CreateTime: 2024-07-10
 * @Description:
 */
@Slf4j
public class AppStater {
    public static void main(String[] args) throws InterruptedException {
        start();
    }


    public static void start() throws InterruptedException {
        new EchoServer().run(8070);
    }

    ;

    /**
     * 测试入站处理器
     */

    public static void test0001() {
        ChannelInitializer<EmbeddedChannel> channelInitializer = new InboundInitializer();
        // 构建EmbeddedChannel，传入channel初始化器
        EmbeddedChannel channel = new EmbeddedChannel(channelInitializer);
        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes("test0001".getBytes());
        // 向通道写一个入站报文
        channel.writeInbound(buf);

    }

    /**
     * 测试出站处理器
     */

    public static void test0002() {
        ChannelInitializer<EmbeddedChannel> channelInitializer = new InboundInitializer();
        // 构建EmbeddedChannel，传入channel初始化器
        EmbeddedChannel channel = new EmbeddedChannel(channelInitializer);
        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes("test0002".getBytes());
        // 向通道写一个入站报文
        channel.writeOutbound(buf);

    }
}

package pers.example.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import lombok.extern.slf4j.Slf4j;
import pers.example.netty.server.handler.InboundInitializer;
import pers.example.netty.server.server.EchoServer;
import pers.example.netty.server.server.FixedServer;

/**
 * @Author: dongcx
 * @CreateTime: 2024-07-10
 * @Description:
 */
@Slf4j
public class AppStater {
    public static void main(String[] args) throws Exception {
        echoServerStart();
    }


    public static void echoServerStart() throws InterruptedException {
        new EchoServer().run(8070);
    }

    public static void fixedServerStart() throws InterruptedException {
        new FixedServer().run(8070);
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

    /**
     * CompositeByteBuf 用于合并多个 ByteBuf。虽然称为合并，但实际上没有发生数据复制，而是通过移动读指针来完成数据合并，是零拷贝的一处体现。
     * 假设 buf1=[1,2,3]，读指针为0，写指针为3；buf2=[6,7,8,9]，读指针为0，写指针为4。那么，合并后的 CompositeByteBuf 读指针为0，写指针为7（3+4）。
     * 读取数据时，读指针会根据偏移量从 buf1 和 buf2 中寻找数据。例如，读指针为1时，读取的是 buf1[1]；读指针为5时，读取的是 buf2[2]（5-3，3是偏移量）
     */
    public static void test0003() {
        // 创建两个独立的 ByteBuf 实例
        ByteBuf buf1 = Unpooled.buffer(10);
        ByteBuf buf2 = Unpooled.buffer(15);

        // 写入一些数据
        buf1.writeBytes(new byte[]{1, 2, 3});
        buf2.writeBytes(new byte[]{6, 7, 8, 9});

        // 创建一个 CompositeByteBuf 实例
        CompositeByteBuf compositeBuf = Unpooled.compositeBuffer();

        // 将 buf1 和 buf2 添加到 compositeBuf 中
        compositeBuf.addComponents(true, buf1, buf2);

        // 读取 compositeBuf 中的数据
        while (compositeBuf.isReadable()) {
            System.out.print(compositeBuf.readByte() + " ");
        }
        // 输出: 1 2 3 4 5 6 7 8 9 10 11 12

        // 释放内存
        compositeBuf.release();
    }

}

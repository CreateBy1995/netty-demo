package pers.example.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelFuture;
import pers.example.netty.client.client.EchoClient;
import pers.example.netty.client.client.FixedClient;
import pers.example.netty.client.client.TestClient;

/**
 * @Author: dongcx
 * @CreateTime: 2024-07-10
 * @Description:
 */
public class AppStater {
    public static void main(String[] args) throws Exception {
        testClientStart();

    }

    public static void echoClientStart() throws InterruptedException {
        new EchoClient().connect(8070);
    }

    public static void fixedClientStart() throws InterruptedException {
        new FixedClient().connect(8070);
    }

    public static void testClientStart() throws InterruptedException {
        ChannelFuture future = new TestClient().connect(8070);
        ByteBufAllocator allocator = ByteBufAllocator.DEFAULT;
        ByteBuf byteBuf = allocator.buffer(8);
        byteBuf.writeBytes("testClientStart".getBytes());
        future.channel().writeAndFlush(byteBuf);
    }
}

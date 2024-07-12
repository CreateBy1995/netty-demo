package pers.example.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import pers.example.netty.client.client.EchoClient;
import pers.example.netty.client.client.FixedClient;

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

    public static void testClientStart() throws Exception {
//        ChannelFuture future = new TestClient().connect(8070);
        ByteBufAllocator allocator = ByteBufAllocator.DEFAULT;
        ByteBuf byteBuf = allocator.buffer(1500 );
        byteBuf = allocator.buffer(1024*29 );
        System.out.println(byteBuf);
//        ByteBuf byteBuf = allocator.buffer(8192 * 2);
//        byteBuf.writeBytes("testClientStart".getBytes());
//        future.channel().writeAndFlush(byteBuf);

//        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
//        FileChannel fileChannel = FileChannel.open(Paths.get("D:\\temp1.txt"), StandardOpenOption.READ);
//        fileChannel.read(buffer);
//        buffer.flip();
//        byte[] bytes = new byte[buffer.remaining()];
//        buffer.get(bytes);
//        System.out.println(new String(bytes));
    }
}

package pers.example.netty.server;

import pers.example.netty.server.server.EchoServer;

/**
 * @Author: dongcx
 * @CreateTime: 2024-07-10
 * @Description:
 */
public class AppStater {
    public static void main(String[] args) throws InterruptedException {
        new EchoServer().run(8070);
    }
}

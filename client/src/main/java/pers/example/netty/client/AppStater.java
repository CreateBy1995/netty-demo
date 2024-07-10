package pers.example.netty.client;

import pers.example.netty.client.client.EchoClient;

/**
 * @Author: dongcx
 * @CreateTime: 2024-07-10
 * @Description:
 */
public class AppStater {
    public static void main(String[] args) throws InterruptedException {
        new EchoClient().connect(8070);
    }
}

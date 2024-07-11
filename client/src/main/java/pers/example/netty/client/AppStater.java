package pers.example.netty.client;

import pers.example.netty.client.client.EchoClient;
import pers.example.netty.client.client.FixedClient;

/**
 * @Author: dongcx
 * @CreateTime: 2024-07-10
 * @Description:
 */
public class AppStater {
    public static void main(String[] args) throws Exception {
        fixedClientStart();
    }

    public static void echoClientStart() throws InterruptedException {
        new EchoClient().connect(8070);
    }

    public static void fixedClientStart() throws InterruptedException {
        new FixedClient().connect(8070);
    }
}

package pers.example.netty;

import pers.example.netty.client.EchoClient;
import pers.example.netty.server.EchoServer;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int port = 8070;
        new Thread(()->{
            EchoServer echoServer = new EchoServer();
            try {
                echoServer.run(port);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        TimeUnit.SECONDS.sleep(1);
        EchoClient echoClient = new EchoClient();
        echoClient.connect(port);
    }
}

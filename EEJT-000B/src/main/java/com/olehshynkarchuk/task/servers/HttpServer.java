package com.olehshynkarchuk.task.servers;


import java.io.IOException;
import java.net.ServerSocket;


public class HttpServer implements AbstractFactoryServer {

    private ServerSocket serverSocket;

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            if (true)
                new HttpRequestHandler(serverSocket.accept()).start();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stop();
        }

    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

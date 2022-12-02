package com.olehshynkarchuk.task.servers;


import com.olehshynkarchuk.task.command.CommandFactory;

import java.io.IOException;
import java.net.ServerSocket;


public class HttpServer implements AbstractFactoryServer {

    private ServerSocket serverSocket;

    public void start(int port, CommandFactory factory) {
        try {
            serverSocket = new ServerSocket(port);
            while (true)
                new HttpRequestHandler(serverSocket.accept(), factory).start();
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

    @Override
    public AbstractFactoryServer create() {
        return new HttpServer();
    }
}

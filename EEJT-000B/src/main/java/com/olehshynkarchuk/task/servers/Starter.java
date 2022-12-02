package com.olehshynkarchuk.task.servers;

public class Starter {
    public static void main(String[] args) {
        int port = 3000;
        AbstractFactoryServer tcpServer = new TCPServer();
        tcpServer.start(port + 1);
        AbstractFactoryServer httpServer = new HttpServer();
        httpServer.start(port);
    }
}

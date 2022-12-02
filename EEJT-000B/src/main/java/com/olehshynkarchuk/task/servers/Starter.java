package com.olehshynkarchuk.task.servers;

public class Starter {
    public static void main(String[] args) {
        int port = 3000;
        TCPServer tcpServer = new TCPServer();
        tcpServer.start(port + 1);
        HttpServer httpServer = new HttpServer();
        httpServer.start(port);
    }
}

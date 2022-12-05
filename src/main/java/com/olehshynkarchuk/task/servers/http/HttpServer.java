package com.olehshynkarchuk.task.servers.http;


import com.olehshynkarchuk.task.command.CommandContainer;
import com.olehshynkarchuk.task.io.ConsoleIO;
import com.olehshynkarchuk.task.servers.AbstractServer;

import java.io.IOException;
import java.net.ServerSocket;


public class HttpServer extends AbstractServer {

    private final CommandContainer commandContainer;
    private ServerSocket serverSocket;
    private final int port;
    private boolean switcher = true;

    public HttpServer(int port, CommandContainer commandContainer) {
        this.port = port;
        this.commandContainer = commandContainer;

    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            while (switcher) {
                new HttpRequestHandler(serverSocket, serverSocket.accept(), commandContainer).start();
            }
        } catch (IOException e) {
            ConsoleIO.printErr(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            stop();
        }

    }

    public void stop() {
        switcher = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            ConsoleIO.printErr(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}

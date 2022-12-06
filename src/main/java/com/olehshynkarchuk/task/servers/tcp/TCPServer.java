package com.olehshynkarchuk.task.servers.tcp;

import com.olehshynkarchuk.task.command.CommandContainer;
import com.olehshynkarchuk.task.io.ConsoleIO;
import com.olehshynkarchuk.task.servers.AbstractServer;

import java.io.IOException;
import java.net.ServerSocket;

public class TCPServer extends AbstractServer {

    private final CommandContainer commandContainer;
    private final int port;
    private ServerSocket serverSocket;
    private boolean switcher = true;

    public TCPServer(int port, CommandContainer commandContainer) {

        this.port = port;
        this.commandContainer = commandContainer;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            while (switcher)
                new TCPRequestHandler(serverSocket.accept(), commandContainer).start();
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

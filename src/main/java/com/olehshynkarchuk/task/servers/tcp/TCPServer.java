package com.olehshynkarchuk.task.servers.tcp;

import com.olehshynkarchuk.task.command.CommandContainer;
import com.olehshynkarchuk.task.io.ConsoleIO;
import com.olehshynkarchuk.task.servers.AbstractServer;

import java.io.IOException;
import java.net.ServerSocket;

public class TCPServer extends AbstractServer {
    public static boolean switcher = true;
    private final CommandContainer commandContainer;
    private ServerSocket serverSocket;
    private final int port;

    public TCPServer(int port, CommandContainer commandContainer) {

        this.port = port;
        this.commandContainer = commandContainer;
    }

    public void start() {
        try {
            this.serverSocket = new ServerSocket(port);
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
        try {
            serverSocket.close();
        } catch (IOException e) {
            ConsoleIO.printErr(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

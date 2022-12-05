package com.olehshynkarchuk.task.servers.tcp;

import com.olehshynkarchuk.task.command.CommandContainer;
import com.olehshynkarchuk.task.io.ConsoleIO;
import com.olehshynkarchuk.task.io.SocketIO;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Locale;

public class TCPRequestHandler extends Thread {

    private final SocketIO socketIO;
    private final CommandContainer commandContainer;
    private final ServerSocket serverSocket;

    public TCPRequestHandler(ServerSocket serverSocket, Socket accept, CommandContainer commandContainer) {
        this.commandContainer = commandContainer;
        this.socketIO = new SocketIO(accept);
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        try {
            socketIO.initIO();
            ConsoleIO.println("TCP START");
            String request = socketIO.readLine();
            String body = socketIO.readLine();
            ConsoleIO.println("TCP REQUEST HEADER : " + request);
            ConsoleIO.println("TCP REQUEST BODY : " + body);
            String method = request.split(" ")[0].toUpperCase(Locale.ROOT).trim();
            socketIO.sendTcpResponds(commandContainer.commandList.get(CommandContainer.Command.getCommand(method, request))
                    .execute(request, body));
            ConsoleIO.println("TCP END");
            socketIO.close();
        } catch (IOException e) {
            ConsoleIO.printErr(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

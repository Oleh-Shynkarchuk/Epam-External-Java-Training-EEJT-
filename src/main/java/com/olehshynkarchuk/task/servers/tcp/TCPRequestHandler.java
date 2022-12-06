package com.olehshynkarchuk.task.servers.tcp;

import com.olehshynkarchuk.task.command.CommandContainer;
import com.olehshynkarchuk.task.io.ConsoleIO;
import com.olehshynkarchuk.task.io.SocketIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Locale;

public class TCPRequestHandler extends Thread {

    private final SocketIO socketIO;
    private final CommandContainer commandContainer;

    public TCPRequestHandler(Socket accept, CommandContainer commandContainer) {
        this.commandContainer = commandContainer;
        this.socketIO = new SocketIO(accept);

    }

    @Override
    public void run() {
        try {
            BufferedReader reader = socketIO.createSocketReader();
            PrintWriter writer = socketIO.createSocketWriter();
            ConsoleIO.println("TCP START");
            String request = socketIO.readLine(reader);
            String body = socketIO.readLine(reader);
            ConsoleIO.println("TCP REQUEST HEADER : " + request);
            ConsoleIO.println("TCP REQUEST BODY : " + body);
            String method = request.split(" ")[0].toUpperCase(Locale.ROOT).trim();
            socketIO.sendTcpResponds(writer, commandContainer.commandList.get(CommandContainer.Command.getCommand(method, request))
                    .execute(request, body));
            ConsoleIO.println("TCP END");
            socketIO.close();
        } catch (IOException e) {
            ConsoleIO.printErr(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

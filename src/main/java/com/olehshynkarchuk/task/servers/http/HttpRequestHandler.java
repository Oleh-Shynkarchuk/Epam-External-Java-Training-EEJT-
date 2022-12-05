package com.olehshynkarchuk.task.servers.http;

import com.olehshynkarchuk.task.command.CommandContainer;
import com.olehshynkarchuk.task.io.ConsoleIO;
import com.olehshynkarchuk.task.io.SocketIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpRequestHandler extends Thread {
    private final SocketIO socketIO;
    private final CommandContainer commandFactory;
    private ServerSocket serverSocket;

    public HttpRequestHandler(ServerSocket serverSocket, Socket socket, CommandContainer factory) {
        this.commandFactory = factory;
        socketIO = new SocketIO(socket);
        this.serverSocket = serverSocket;
    }

    public void run() {
        try {
            BufferedReader reader = socketIO.createSocketReader();
            PrintWriter writer = socketIO.createSocketWriter();
            String requestStartLine = socketIO.lines(reader).filter(lines -> lines.contains("HTTP/")).findFirst().orElse(null);
            if (requestStartLine != null) {
                ConsoleIO.println("HTTP START");
                String[] splitRequest = requestStartLine.split(" ");
                String httpMethod = splitRequest[0].trim();
                String requestHead = splitRequest[1].trim();
                String httpVersion = splitRequest[2].trim();
                String requestBodyData = "";
                requestBodyData = getRequestBodyDataIfNeed(reader, httpMethod, requestBodyData);
                ConsoleIO.println("HTTP HEADER REQUEST: " + requestStartLine);
                ConsoleIO.println("HTTP BODY REQUEST: " + requestBodyData);
                socketIO.sendHttpResponds(writer, httpVersion, commandFactory.commandList.get(CommandContainer.Command.getCommand(httpMethod, requestHead))
                        .execute(requestHead, requestBodyData));
                ConsoleIO.println("HTTP END");
            }
            socketIO.close();
        } catch (IOException e) {
            ConsoleIO.printErr(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private String getRequestBodyDataIfNeed(BufferedReader reader, String httpMethod, String requestBodyData) {
        if (httpMethod.equals("POST") || httpMethod.equals("PUT")) {
            String contentLengthLine = socketIO.lines(reader).filter(lines -> lines.contains("Content-Length:")).findFirst().orElse(null);
            int contentLength = Integer.parseInt(contentLengthLine != null ? contentLengthLine.substring(contentLengthLine.indexOf("Content-Length:") + 16) : "0");
            if (contentLength > 0) {
                socketIO.readLine(reader);
                char[] bodyCharArray = new char[contentLength];
                if (socketIO.read(reader, bodyCharArray, 0, contentLength) == contentLength) {
                    requestBodyData = new String(bodyCharArray);
                }
            }
        }
        return requestBodyData;
    }
}
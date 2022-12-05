package com.olehshynkarchuk.task.servers.http;

import com.olehshynkarchuk.task.command.CommandContainer;
import com.olehshynkarchuk.task.io.ConsoleIO;
import com.olehshynkarchuk.task.io.SocketIO;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpRequestHandler extends Thread {
    private final SocketIO socketIO;
    private final CommandContainer commandFactory;
    private ServerSocket serverSocket;

    public HttpRequestHandler(ServerSocket serverSocket, Socket socket, CommandContainer factory) {
        System.out.println("new Handler");
        this.commandFactory = factory;
        socketIO = new SocketIO(socket);
        this.serverSocket = serverSocket;
    }

    public void run() {
        try {
            socketIO.initIO();
            String requestStartLine = socketIO.lines().filter(lines -> lines.contains("HTTP/")).findFirst().orElse(null);
            if (requestStartLine != null) {
                ConsoleIO.println("HTTP START");
                String[] splitRequest = requestStartLine.split(" ");
                String httpMethod = splitRequest[0].trim();
                String requestHead = splitRequest[1].trim();
                String httpVersion = splitRequest[2].trim();
                String requestBodyData = "";
                requestBodyData = getRequestBodyDataIfNeed(httpMethod, requestBodyData);
                ConsoleIO.println("HTTP HEADER REQUEST: " + requestStartLine);
                ConsoleIO.println("HTTP BODY REQUEST: " + requestBodyData);
                socketIO.sendHttpResponds(httpVersion, commandFactory.commandList.get(CommandContainer.Command.getCommand(httpMethod, requestHead))
                        .execute(requestHead, requestBodyData));
                ConsoleIO.println("HTTP END");
            }
            socketIO.close();
        } catch (IOException e) {
            ConsoleIO.printErr(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private String getRequestBodyDataIfNeed(String httpMethod, String requestBodyData) {
        if (httpMethod.equals("POST") || httpMethod.equals("PUT")) {
            String contentLengthLine = socketIO.lines().filter(lines -> lines.contains("Content-Length:")).findFirst().orElse(null);
            int contentLength = Integer.parseInt(contentLengthLine != null ? contentLengthLine.substring(contentLengthLine.indexOf("Content-Length:") + 16) : "0");
            if (contentLength > 0) {
                socketIO.readLine();
                char[] bodyCharArray = new char[contentLength];
                if (socketIO.read(bodyCharArray, 0, contentLength) == contentLength) {
                    requestBodyData = new String(bodyCharArray);
                }
            }
        }
        return requestBodyData;
    }
}
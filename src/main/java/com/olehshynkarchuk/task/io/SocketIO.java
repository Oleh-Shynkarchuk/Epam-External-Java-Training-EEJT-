package com.olehshynkarchuk.task.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Stream;

public class SocketIO implements AutoCloseable {
    private final Socket socket;
    private PrintWriter output;
    private BufferedReader input;
    private final static String PREFIX = "<";
    private final static String POSTFIX = ">";

    public SocketIO(Socket socket) {
        this.socket = socket;
    }

    public void initIO() {
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            output = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            ConsoleIO.printErr(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String readLine() {
        try {
            return input.readLine();
        } catch (IOException e) {
            ConsoleIO.printErr(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void sendTcpResponds(Map<Integer, String> message) {
        output.println(PREFIX + message.entrySet().iterator().next().getValue() + POSTFIX);
    }


    @Override
    public void close() {
        output.close();
        try {
            socket.close();
            input.close();
        } catch (IOException e) {
            ConsoleIO.printErr(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Stream<String> lines() {
        return input.lines();
    }

    public int read(char[] bodyCharArray, int i, int contentLength) {
        try {
            return input.read(bodyCharArray, i, contentLength);
        } catch (IOException e) {
            ConsoleIO.printErr(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void sendHttpResponds(String httpVersion, Map<Integer, String> commandResult) {
        output.println(httpVersion + " " + commandResult.entrySet().iterator().next().getKey());
        output.println("Content-Type: application/json; charset=utf-8");
        output.println("");
        output.println(commandResult.entrySet().iterator().next().getValue());
    }
}

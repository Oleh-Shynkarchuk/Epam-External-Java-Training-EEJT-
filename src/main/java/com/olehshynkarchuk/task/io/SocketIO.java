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

    public BufferedReader createSocketReader() {
        try {
            input = new BufferedReader(new InputStreamReader(
                    socket.getInputStream(), StandardCharsets.UTF_8));
            return input;
        } catch (IOException e) {
            ConsoleIO.printErr(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public PrintWriter createSocketWriter() {
        try {
            output = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);
            return output;
        } catch (IOException e) {
            ConsoleIO.printErr(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String readLine(BufferedReader input) {
        try {
            return input.readLine();
        } catch (IOException e) {
            ConsoleIO.printErr(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void sendTcpResponds(PrintWriter output, Map<Integer, String> message) {
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

    public Stream<String> lines(BufferedReader input) {
        return input.lines();
    }

    public int read(BufferedReader input, char[] bodyCharArray, int i, int contentLength) {
        try {
            return input.read(bodyCharArray, i, contentLength);
        } catch (IOException e) {
            ConsoleIO.printErr(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void sendHttpResponds(PrintWriter output, String httpVersion, Map<Integer, String> commandResult) {
        output.println(httpVersion + " " + commandResult.entrySet().iterator().next().getKey());
        output.println("Content-Type: application/json; charset=utf-8");
        output.println("");
        output.println(commandResult.entrySet().iterator().next().getValue());
    }
}

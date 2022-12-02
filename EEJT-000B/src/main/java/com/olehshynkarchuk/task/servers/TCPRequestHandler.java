package com.olehshynkarchuk.task.servers;

import com.olehshynkarchuk.task.command.CommandFactory;
import com.olehshynkarchuk.task.goods.Goods;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TCPRequestHandler extends Thread {

    private final Socket clientSocket;
    private BufferedReader input;
    private PrintWriter output;
    private final String prefix = "<";
    private final String postfix = ">";

    public TCPRequestHandler(Socket accept, CommandFactory factory) {
        this.clientSocket = accept;
    }

    @Override
    public void run() {
        try {
            initWriterReader();
            String word = input.readLine();
            if (word.equals("get count")) {
                System.out.println("command" + commandFactory.commandList.get(CommandFactory.Commands.GOODSSIZE).execute(word, repository));
                output.write(prefix +
                        commandFactory.commandList.get(CommandFactory.Commands.GOODSSIZE).execute(word, repository)
                        + postfix);
                output.flush();
            } else if (word.matches("get item=\\d+")) {
                String s = (String.join("", word.split("\\D+")));
                output.write(prefix + commandFactory.commandList.get(CommandFactory.Commands.GOODSNAMEANDPRICE).execute(s, repository) + postfix);
                output.flush();
            } else if (word.equals("put item")) {
                Goods goods = new Goods("newTcpProduct", -300);
                output.write(prefix +
                        commandFactory.commandList.get(CommandFactory.Commands.NEWGOODS).execute(mapper.writeValueAsString(goods), repository)
                        + postfix);
                output.flush();
            } else {
                output.write("Wrong input : " + word + "\n");
                output.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                closeClientRequest();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initWriterReader() throws IOException {
        input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
        output = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    private void closeClientRequest() throws IOException {
        input.close();
        output.close();
        clientSocket.close();
    }
}

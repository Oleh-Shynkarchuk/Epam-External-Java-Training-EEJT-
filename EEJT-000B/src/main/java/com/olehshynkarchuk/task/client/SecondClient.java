package com.olehshynkarchuk.task.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SecondClient {
    private static Socket clientSocket;
    private static BufferedReader in;
    private static PrintWriter out;

    public static void main(String[] args) {
        try {
            try {
                clientSocket = new Socket("localhost", 3001);
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter((clientSocket.getOutputStream()));
                System.out.println("Передати повідомлення на сервак:");

                String word = reader.readLine();

                out.write(word + "\n");
                out.flush();
                String serverWord = in.readLine();
                System.out.println(serverWord);
            } finally {
                System.out.println("Клієнт закритий...");
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.olehshynkarchuk.task.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static Socket clientSocket;
    private static BufferedReader in;
    private static PrintWriter out;

    public static void main(String[] args) {
        String start = "1";
        while (start.equals("1")) {
            try {
                try {
                    clientSocket = new Socket("localhost", 3001);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    out = new PrintWriter((clientSocket.getOutputStream()));
                    System.out.println("Доступні команди:");
                    System.out.println("Запит щоб вивести інформацію про товар за його номером :\n" +
                            "get item=ID");
                    System.out.println("Запит щоб додати новий товар (необхідно заповнити обєкт) :\n" +
                            "#new item");
                    System.out.println("Запит щоб оновити існуючий товар (необхідно заповнити обєкт) :\n" +
                            "#put item=ID");
                    System.out.println("Запит щоб видалити новий товар за його номером :\n" +
                            "delete item=ID");
                    System.out.println("Запит щоб вивести інформацію про кількість унікальніних товарів :\n" +
                            "get count");
                    System.out.println("Запит щоб вивести інформацію про усі товари :\n" +
                            "get items");
                    String word = reader.readLine();
                    out.write(word + "\n");
                    System.out.println("За необхідності заповніть обєкт запиту у форматі json" +
                            "(методи які починаються на '#') в іншому разі можете відправити будь-що.\n" +
                            "Приклад : { \"name\": \"newProduct\", \"price\": 25.31 }");
                    String word2 = reader.readLine() + "";
                    out.write(word2 + "\n");
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
            Scanner scanner = new Scanner(System.in);
            System.out.println("Щоб продовжити відправте цифру 1, щоб завершити будь-що інше.");
            start = scanner.nextLine();
        }
    }
}

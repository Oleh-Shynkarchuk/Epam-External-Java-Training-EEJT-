package com.olehshynkarchuk.task.servers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olehshynkarchuk.task.command.CommandFactory;
import com.olehshynkarchuk.task.goods.Goods;
import com.olehshynkarchuk.task.goods.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

public class HttpRequestHandler extends Thread {
    private final Socket clientSocket;
    private PrintWriter output;
    private BufferedReader input;

    public HttpRequestHandler(Socket socket) {
        this.clientSocket = socket;
    }

    public void run() {
        try {
            initWriterReader();
            Repository repository = new Repository();
            CommandFactory commandFactory = new CommandFactory();
            String requestStartLine = getRequestStartLine();
            System.out.println(requestStartLine);
            if (!requestStartLine.equals("")) {
                StringTokenizer parse = new StringTokenizer(requestStartLine);
                String httpMethod = parse.nextToken().toUpperCase().trim();
                String request = parse.nextToken().toLowerCase(Locale.ROOT).trim();
                String httpVersion = parse.nextToken().trim();
                if (httpMethod.equals("POST")) {
                    if (request.equals("/shop/insert_item")) {
                        Goods goods = getNewProductFromRequest();
                        repository.putItem(goods);
                    }
                } else if (httpMethod.equals("GET")) {
                    ObjectMapper mapper = new ObjectMapper();
                    if (request.equals("/shop/count")) {
                        okRequestResponds();
                        output.println(mapper.writeValueAsString(commandFactory.commandList.get(CommandFactory.Commands.GOODSNAMEANDPRICE).execute(request, repository)));
                    } else if (request.contains("/shop/item\\?get_info=")) {
                        if (request.matches("/shop/item\\?get_info=\\d+")) {
                            String id = request.replaceAll("\\D", "");
                            okRequestResponds();
                            output.println(mapper.writeValueAsString(commandFactory.commandList.get(CommandFactory.Commands.GOODSNAMEANDPRICE).execute(id, repository)));
                        } else {
                            output.println("HTTP/1.1 400 Bad Request");
                            output.println("Connection: close");
                            output.println();
                            output.println(mapper.writeValueAsString("Wrong input"));
                        }

                    } else if (request.equals("/shop/insert_item")) {
                        okRequestResponds();
                        output.println(mapper.writeValueAsString(commandFactory.commandList.get(CommandFactory.Commands.ALLGOODS).execute(request, repository)));
                    } else {
                        output.println("HTTP/1.1 404 Not found");
                        output.println("Connection: close");
                        output.println();
                        output.println(mapper.writeValueAsString("Page 404"));
                    }
                }
            }
            System.out.println("Request end!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println("closing");
                closeClientRequest();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Goods getNewProductFromRequest() throws IOException {
        String fieldName = "";
        boolean switchToNextField = false;
        Map<String, String> mapForGoodsFields = new HashMap<>();
        while (input.ready()) {
            String lines = input.readLine();
            System.out.println(lines);
            if (switchToNextField) {
                if (lines.length() > 0) {
                    switchToNextField = false;
                    mapForGoodsFields.put(fieldName, lines);
                }
            }
            if (lines.contains("Content-Disposition:")) {
                StringTokenizer parser = new StringTokenizer(lines);
                while (parser.hasMoreTokens()) {
                    fieldName = parser.nextToken();
                    if (fieldName.contains("name=")) {
                        String[] s = fieldName.split("=");
                        fieldName = s[1].replaceAll("[^\\w+]", "");
                        switchToNextField = true;
                    }
                }
            }
        }
        return new Goods(mapForGoodsFields.get("name"),
                Double.parseDouble(mapForGoodsFields.get("price")));
    }

    private void okRequestResponds() {
        output.println("HTTP/1.1 200 OK");
        output.println("Content-Type: application/json; charset=utf-8");
        output.println();
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

    private String getRequestStartLine() throws IOException {
        if (input.ready()) {
            return input.readLine();
        }
        return "";
    }
}
package com.olehshynkarchuk.task.servers;

import com.olehshynkarchuk.task.command.CommandFactory;

import java.io.IOException;
import java.net.ServerSocket;

public class TCPServer implements AbstractFactoryServer {
    private ServerSocket serverSocket;

    public void start(int port, CommandFactory factory) {
        try {
            this.serverSocket = new ServerSocket(port);
            while (true)
                new TCPRequestHandler(serverSocket.accept(), factory).start();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stop();
        }
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public AbstractFactoryServer create() {
        return new TCPServer();
    }
}

//             ServerSocket serverSocket2 = new ServerSocket(3001)
//            Server.clientSocket = serverSocket2.accept();
//            try {
//                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//                out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
//                String word = in.readLine();
//                while (!word.equals("end")) {
////                    Command command= Controller.getCommand(word,repository);
////                    if (command != null) {
////                        command.execute();
////                    }
//                    System.out.println(word);
//                    if (word.equals("get count")) {
//                        CommandProductSize productSizeCommand=new CommandProductSize(repository);
//                        productSizeCommand.execute();
//                        out.write(productSizeCommand.getCount() + "\n");
//                    } else if (word.contains("get item=")) {
//                        int id = Integer.parseInt(String.join("", word.split("\\D+")));
//                        out.write(repository.getItem(id).getName()
//                                + "|" + repository.getItem(id).getPrice() +
//                                "\n");
//
//                    } else {
//                        out.write("Wrong input : " + word + "\n");
//                    }
//                }
//            } finally {
//                clientSocket.close();
//                in.close();
//                out.close();
//            }

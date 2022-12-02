package com.olehshynkarchuk.task.servers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olehshynkarchuk.task.command.CommandFactory;
import com.olehshynkarchuk.task.goods.Repository;

public class Starter {
    public static void main(String[] args) {
        CommandFactory commandFactory = getCommandFactory();
        int port = 3000;
        AbstractFactoryServer server1 = new HttpServer().create();
        server1.start(port, commandFactory);
        AbstractFactoryServer server2 = new TCPServer().create();
        server2.start(port, commandFactory);
    }

    private static CommandFactory getCommandFactory() {
        Repository repository = new Repository();
        ObjectMapper mapper = new ObjectMapper();
        return new CommandFactory(repository, mapper);
    }
}

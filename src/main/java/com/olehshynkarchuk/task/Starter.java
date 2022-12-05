package com.olehshynkarchuk.task;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.olehshynkarchuk.task.command.CommandContainer;
import com.olehshynkarchuk.task.goods.GoodsRepository;
import com.olehshynkarchuk.task.servers.AbstractServer;
import com.olehshynkarchuk.task.servers.http.HttpServerFactory;
import com.olehshynkarchuk.task.servers.tcp.TcpServerFactory;

public class Starter {
    public static void main(String[] args) {
        CommandContainer commandFactory = getCommandFactory();
        int port = 3000;

        AbstractServer server1 = new HttpServerFactory().createServer(port, commandFactory);
        AbstractServer server2 = new TcpServerFactory().createServer(port + 1, commandFactory);
    }

    private static CommandContainer getCommandFactory() {
        GoodsRepository goodsRepository = new GoodsRepository();
        JsonMapper mapper = new JsonMapper();
        return new CommandContainer(goodsRepository, mapper);
    }
}

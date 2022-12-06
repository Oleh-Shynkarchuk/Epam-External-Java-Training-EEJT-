package com.olehshynkarchuk.task.servers.http;

import com.olehshynkarchuk.task.command.CommandContainer;
import com.olehshynkarchuk.task.servers.AbstractServer;
import com.olehshynkarchuk.task.servers.AbstractServerFactory;
import com.olehshynkarchuk.task.servers.ServerRunner;

public class HttpServerFactory implements AbstractServerFactory {

    @Override
    public AbstractServer createServer(int port, CommandContainer factory) {
        HttpServer server = new HttpServer(port, factory);
        new ServerRunner(server).start();
        return server;
    }
}

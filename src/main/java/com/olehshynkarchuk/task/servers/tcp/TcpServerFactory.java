package com.olehshynkarchuk.task.servers.tcp;

import com.olehshynkarchuk.task.command.CommandContainer;
import com.olehshynkarchuk.task.servers.AbstractServer;
import com.olehshynkarchuk.task.servers.AbstractServerFactory;
import com.olehshynkarchuk.task.servers.ServerRunner;

public class TcpServerFactory implements AbstractServerFactory {
    @Override
    public AbstractServer createServer(int port, CommandContainer factory) {
        TCPServer server = new TCPServer(port, factory);
        new ServerRunner(server).start();
        return server;
    }
}

package com.olehshynkarchuk.task.servers;

public class ServerRunner extends Thread {
    private final AbstractServer abstractServer;

    public ServerRunner(AbstractServer abstractServer) {
        this.abstractServer = abstractServer;
    }

    @Override
    public void run() {
        abstractServer.start();
    }
}

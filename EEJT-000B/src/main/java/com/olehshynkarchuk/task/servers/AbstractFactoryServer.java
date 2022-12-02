package com.olehshynkarchuk.task.servers;

public interface AbstractFactoryServer {
    void start(int port);

    void stop();
}

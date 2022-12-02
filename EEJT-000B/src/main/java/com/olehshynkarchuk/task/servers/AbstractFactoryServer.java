package com.olehshynkarchuk.task.servers;

import com.olehshynkarchuk.task.command.CommandFactory;

public interface AbstractFactoryServer {
     void start(int port, CommandFactory factory);

     void stop();

     AbstractFactoryServer create();
}

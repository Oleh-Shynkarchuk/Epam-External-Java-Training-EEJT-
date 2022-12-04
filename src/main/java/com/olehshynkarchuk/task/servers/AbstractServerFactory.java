package com.olehshynkarchuk.task.servers;

import com.olehshynkarchuk.task.command.CommandContainer;

public interface AbstractServerFactory {
    AbstractServer createServer(int port, CommandContainer factory);
}

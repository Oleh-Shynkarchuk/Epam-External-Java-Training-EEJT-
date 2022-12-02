package com.olehshynkarchuk.task.command;

import java.util.Map;

public class CommandFactory {
    public Map<Commands, Command> commandList;

    public CommandFactory() {
        commandList = Map.of(
                Commands.GOODSSIZE, new CommandProductSize(),
                Commands.GOODSNAMEANDPRICE, new CommandProductNameAndPrice(),
                Commands.NEWGOODS, new CommandNewGoods(),
                Commands.ALLGOODS, new CommandAllGoods());
    }

    public enum Commands {
        GOODSSIZE, GOODSNAMEANDPRICE, NEWGOODS, ALLGOODS
    }
}

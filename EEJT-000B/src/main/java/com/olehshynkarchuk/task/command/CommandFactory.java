package com.olehshynkarchuk.task.command;

import com.olehshynkarchuk.task.repo.Repository;

import java.util.Map;

public class CommandFactory {
    private static CommandFactory instance;
    public Map<Commands, Command> commandList;
    Repository repository;

    public CommandFactory(Repository repository) {
        this.repository = repository;
        commandList = Map.of(
                Commands.GOODSSIZE, new CommandProductSize(),
                Commands.GOODSNAMEANDPRICE, new CommandProductNameAndPrice(),
                Commands.NEWGOODS, new CommandNewGoods(),
                Commands.ALLGOODS, new CommandAllGoods());
    }

    public static CommandFactory getInstance(Repository repository) {
        if (instance == null) {
            instance = new CommandFactory(repository);

        }
        return instance;
    }

    public enum Commands {
        GOODSSIZE, GOODSNAMEANDPRICE, NEWGOODS, ALLGOODS
    }
}

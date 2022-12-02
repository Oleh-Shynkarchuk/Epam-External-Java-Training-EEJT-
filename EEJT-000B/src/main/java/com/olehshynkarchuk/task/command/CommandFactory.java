package com.olehshynkarchuk.task.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olehshynkarchuk.task.goods.Repository;

import java.util.Map;

public class CommandFactory {
    public Map<Commands, Command<String>> commandList;

    public CommandFactory(Repository repository, ObjectMapper mapper) {
        commandList = Map.of(
                Commands.GOODSSIZE, new CommandProductSize(repository),
                Commands.GOODSNAMEANDPRICE, new CommandProductNameAndPrice(repository),
//                Commands.NEWGOODS, new CommandNewGoods(repository),
                Commands.ALLGOODS, new CommandAllGoods(repository));
    }

    public enum Commands {
        GOODSSIZE, GOODSNAMEANDPRICE, NEWGOODS, ALLGOODS
    }
}

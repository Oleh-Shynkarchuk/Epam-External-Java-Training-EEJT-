package com.olehshynkarchuk.task.command;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.olehshynkarchuk.task.goods.GoodsRepository;

import java.util.Map;

public class CommandContainer {

    public Map<Command, com.olehshynkarchuk.task.command.Command> commandList;

    public CommandContainer(GoodsRepository goodsRepository, JsonMapper jsonMapper) {
        commandList = Map.of(
                Command.GOODS_REPO_SIZE, new GoodsSizeCommand(goodsRepository, jsonMapper),
                Command.SINGLE_GOODS, new SearchSingleGoodsCommand(goodsRepository, jsonMapper),
                Command.UNKNOWN, new UnknownCommand(jsonMapper),
                Command.NEW_GOODS, new NewGoodsCommand(goodsRepository, jsonMapper),
                Command.ALL_GOODS, new GoodsAllCommand(goodsRepository, jsonMapper),
                Command.DELETE_GOODS, new DeleteGoodsCommand(goodsRepository, jsonMapper),
                Command.REPLACE_GOODS, new ReplaceGoodsCommand(goodsRepository, jsonMapper));
    }

    public enum Command {

        GOODS_REPO_SIZE, SINGLE_GOODS, NEW_GOODS, ALL_GOODS, DELETE_GOODS, UNKNOWN, REPLACE_GOODS;

        public static Command getCommand(String Method, String request) {
            if (Method.equals("GET")) {
                if (request.equals("/shop/items/count") || request.equals("get count")) {
                    return GOODS_REPO_SIZE;
                } else if (request.matches("/shop/item/\\d+/get") || request.matches("get item=\\d+")) {
                    return SINGLE_GOODS;
                } else if (request.equals("/shop/items") || request.equals("get items")) {
                    return ALL_GOODS;
                }
            }
            if (Method.equals("DELETE")) {
                if (request.matches("/shop/item/\\d+/delete") || request.matches("delete item=\\d+")) {
                    return DELETE_GOODS;
                }
            }
            if (Method.equals("PUT")) {
                if (request.matches("/shop/item/\\d+/put") || request.matches("put item=\\d+")) {
                    return REPLACE_GOODS;
                }
            }
            if (Method.equals("POST") || Method.equals("NEW")) {
                if (request.equals("/shop/item/new") || request.contains("new item")) {
                    return NEW_GOODS;
                }
            }
            return UNKNOWN;
        }
    }
}

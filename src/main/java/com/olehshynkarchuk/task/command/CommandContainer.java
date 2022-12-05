package com.olehshynkarchuk.task.command;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.olehshynkarchuk.task.goods.GoodsRepository;

import java.util.Arrays;
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

        GOODS_REPO_SIZE("GET", "/shop/items/count", "get count"),
        SINGLE_GOODS("GET", "/shop/item/get/", "get item="),
        ALL_GOODS("GET", "/shop/items", "get items"),
        NEW_GOODS("POST", "/shop/item/new", "new item"),
        DELETE_GOODS("DELETE", "/shop/item/delete/", "delete item="),
        REPLACE_GOODS("PUT", "/shop/item/put/", "put item="),
        UNKNOWN("", "", "");

        private final String url;
        private final String requestHttp;
        private final String requestTcp;

        Command(String url, String requestHttp, String requestTcp) {
            this.url = url;
            this.requestHttp = requestHttp;
            this.requestTcp = requestTcp;
        }

        public static Command getCommand(String Method, String request) {

            return Arrays.stream(Command.values()).filter(command -> command.url.equals(Method))
                    .filter(command -> request.contains(command.requestHttp) || request.contains(command.requestTcp)).findFirst().orElse(UNKNOWN);
        }
    }
}

package com.olehshynkarchuk.task.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.olehshynkarchuk.task.goods.Goods;
import com.olehshynkarchuk.task.goods.GoodsRepository;

import java.util.Map;

public record DeleteGoodsCommand(GoodsRepository goodsRepository, JsonMapper jsonMapper) implements Command {
    @Override
    public Map<Integer, String> execute(String requestHead, String requestBody) throws JsonProcessingException {

        Goods good = goodsRepository().deleteGoodsByID(Integer.parseInt(String.join("", requestHead.split("\\D+"))));
        if (good == null) return Map.of(409, jsonMapper().writeValueAsString(
                Map.of(409, "Error:Goods with this ID doesn't exist in the Repository")));
        else return Map.of(200, jsonMapper().writeValueAsString(good));
    }
}

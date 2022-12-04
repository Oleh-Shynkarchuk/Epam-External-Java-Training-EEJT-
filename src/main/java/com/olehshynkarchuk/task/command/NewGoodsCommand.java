package com.olehshynkarchuk.task.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.olehshynkarchuk.task.goods.Goods;
import com.olehshynkarchuk.task.goods.GoodsRepository;

import java.util.Map;

public record NewGoodsCommand(GoodsRepository goodsRepository, JsonMapper jsonMapper) implements Command {

    @Override
    public Map<Integer, String> execute(String requestHead, String requestBody) throws JsonProcessingException {
        Goods goods = jsonMapper.readValue(requestBody, Goods.class);
        if (goods != null && goods.price() > 0) {
            return Map.of(200, jsonMapper.writeValueAsString(goodsRepository.putItem(goods)));
        } else if (goods != null && goods.price() < 0) {
            return Map.of(422, jsonMapper.writeValueAsString
                    (Map.of(422, "Unprocessable Entity : Price of Goods cannot be negative")));
        } else
            return Map.of(400, jsonMapper.writeValueAsString(Map.of(400, "Error:Bad Request")));
    }
}

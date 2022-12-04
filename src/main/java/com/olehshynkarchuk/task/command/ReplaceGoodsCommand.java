package com.olehshynkarchuk.task.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.olehshynkarchuk.task.goods.Goods;
import com.olehshynkarchuk.task.goods.GoodsRepository;

import java.util.Map;

public class ReplaceGoodsCommand implements Command {
    private final GoodsRepository goodsRepository;
    private final JsonMapper jsonMapper;

    public ReplaceGoodsCommand(GoodsRepository goodsRepository, JsonMapper jsonMapper) {
        this.goodsRepository = goodsRepository;
        this.jsonMapper = jsonMapper;
    }

    @Override
    public Map<Integer, String> execute(String requestHead, String requestBody) throws JsonProcessingException {

        Goods goodsOnUpdate = jsonMapper.readValue(requestBody, Goods.class);

        if (isaAllGoodsFieldsInit(goodsOnUpdate)) {
            int goodsID = Integer.parseInt(String.join("", requestHead.split("\\D+")));
            if (goodsOnUpdate.price() < 0) {
                return Map.of(422, jsonMapper.writeValueAsString
                        (Map.of(422, "Unprocessable Entity : Price of Goods cannot be negative")));
            }
            Goods goodsOnReturn = goodsRepository.updateGoodsByID(goodsID, goodsOnUpdate);
            return goodsOnReturn == null ? Map.of(409, "Error:Goods with this ID doesn't exist in the Repository")
                    : Map.of(200, jsonMapper.writeValueAsString(goodsOnReturn));
        } else {
            return Map.of(422, jsonMapper.writeValueAsString
                    (Map.of(422, "Unprocessable Entity : " +
                            " You need to specify all fields" +
                            " of the Goods class for the PUT method")));
        }
    }

    private boolean isaAllGoodsFieldsInit(Goods goodsOnUpdate) {
        return goodsOnUpdate.name() != null && goodsOnUpdate.price() != 0.0;
    }
}

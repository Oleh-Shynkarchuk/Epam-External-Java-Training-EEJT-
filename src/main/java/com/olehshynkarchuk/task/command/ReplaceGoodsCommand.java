package com.olehshynkarchuk.task.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.olehshynkarchuk.task.goods.Goods;
import com.olehshynkarchuk.task.goods.GoodsRepository;

import java.util.Map;

import static com.olehshynkarchuk.task.constant.Constants.HttpMessageResponse.*;
import static com.olehshynkarchuk.task.constant.Constants.HttpStatusCodeResponse.*;

public record ReplaceGoodsCommand(GoodsRepository goodsRepository,
                                  JsonMapper jsonMapper) implements Command {

    @Override
    public Map<Integer, String> execute(String requestHead, String requestBody) throws JsonProcessingException {

        Goods newGoods = jsonMapper.readValue(requestBody, Goods.class);

        if (isaAllGoodsFieldsInit(newGoods)) {
            int goodsID = Integer.parseInt(String.join("", requestHead.split("[^\\d-]")));
            if (newGoods.price() < 0) {
                return Map.of(UNPROCESSABLE_ENTITY_CODE, jsonMapper.writeValueAsString
                        (Map.of(UNPROCESSABLE_ENTITY_CODE, UNPROCESSABLE_ENTITY_MESSAGE)));
            }
            Goods goodsOnReturn = goodsRepository.replaceGoodsByID(goodsID, newGoods);
            return goodsOnReturn == null ? Map.of(CONFLICT_CODE, jsonMapper.writeValueAsString(Map.of(CONFLICT_CODE, CONFLICT_MESSAGE)))
                    : Map.of(OK_CODE, jsonMapper.writeValueAsString(goodsOnReturn));
        } else {
            return Map.of(BAD_REQUEST_CODE, jsonMapper.writeValueAsString
                    (Map.of(BAD_REQUEST_CODE, BAD_REQUEST_MESSAGE)));
        }
    }

    private boolean isaAllGoodsFieldsInit(Goods goods) {
        return (goods.name() != null && !goods.name().equals(""));
    }
}

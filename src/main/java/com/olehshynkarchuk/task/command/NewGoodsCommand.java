package com.olehshynkarchuk.task.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.olehshynkarchuk.task.goods.Goods;
import com.olehshynkarchuk.task.goods.GoodsRepository;

import java.util.Map;

import static com.olehshynkarchuk.task.constant.Constants.HttpMessageResponse.BAD_REQUEST_MESSAGE;
import static com.olehshynkarchuk.task.constant.Constants.HttpMessageResponse.UNPROCESSABLE_ENTITY_MESSAGE;
import static com.olehshynkarchuk.task.constant.Constants.HttpStatusCodeResponse.*;

public record NewGoodsCommand(GoodsRepository goodsRepository,
                              JsonMapper jsonMapper) implements Command {

    @Override
    public Map<Integer, String> execute(String requestHead, String requestBody) throws JsonProcessingException {
        Goods goods = jsonMapper.readValue(requestBody, Goods.class);
        if (isaAllGoodsFieldsInit(goods)) {
            if (goods.price() > 0) {
                goods = goodsRepository.putItem(goods);
                return Map.of(OK_CODE, jsonMapper.writeValueAsString(goods));
            } else return Map.of(UNPROCESSABLE_ENTITY_CODE,
                    jsonMapper.writeValueAsString(
                            Map.of(UNPROCESSABLE_ENTITY_CODE, UNPROCESSABLE_ENTITY_MESSAGE)));
        } else return Map.of(BAD_REQUEST_CODE,
                jsonMapper.writeValueAsString(
                        Map.of(BAD_REQUEST_CODE, BAD_REQUEST_MESSAGE)));
    }

    private boolean isaAllGoodsFieldsInit(Goods goods) {
        return (goods.name() != null && !goods.name().equals(""));
    }

}

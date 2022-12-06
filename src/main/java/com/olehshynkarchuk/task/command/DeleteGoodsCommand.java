package com.olehshynkarchuk.task.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.olehshynkarchuk.task.goods.Goods;
import com.olehshynkarchuk.task.goods.GoodsRepository;

import java.util.Map;

import static com.olehshynkarchuk.task.constant.Constants.HttpMessageResponse.BAD_REQUEST_MESSAGE;
import static com.olehshynkarchuk.task.constant.Constants.HttpMessageResponse.CONFLICT_MESSAGE;
import static com.olehshynkarchuk.task.constant.Constants.HttpStatusCodeResponse.*;

public record DeleteGoodsCommand(GoodsRepository goodsRepository, JsonMapper jsonMapper) implements Command {

    @Override
    public Map<Integer, String> execute(String requestHead, String requestBody) throws JsonProcessingException {
        String checkNegativeID = String.join("", requestHead.split("[^\\d-]"));
        if (checkNegativeID.matches("\\d+")) {
            Goods good = goodsRepository().deleteGoodsByID(Integer.parseInt(checkNegativeID));
            if (good == null) return Map.of(CONFLICT_CODE, jsonMapper().writeValueAsString(
                    Map.of(CONFLICT_CODE, CONFLICT_MESSAGE)));
            else return Map.of(OK_CODE, jsonMapper().writeValueAsString(good));
        } else return Map.of(BAD_REQUEST_CODE, jsonMapper().writeValueAsString
                (Map.of(BAD_REQUEST_CODE, BAD_REQUEST_MESSAGE)));
    }
}

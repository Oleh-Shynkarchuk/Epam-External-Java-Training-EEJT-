package com.olehshynkarchuk.task.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.olehshynkarchuk.task.goods.Goods;
import com.olehshynkarchuk.task.goods.GoodsRepository;

import java.util.Map;

import static com.olehshynkarchuk.task.constant.Constants.HttpMessageResponse.CONFLICT_MESSAGE;
import static com.olehshynkarchuk.task.constant.Constants.HttpStatusCodeResponse.CONFLICT_CODE;
import static com.olehshynkarchuk.task.constant.Constants.HttpStatusCodeResponse.OK_CODE;

public record SearchSingleGoodsCommand(GoodsRepository goodsRepository, JsonMapper jsonMapper) implements Command {

    @Override
    public Map<Integer, String> execute(String requestHead, String requestBody) throws JsonProcessingException {
        Goods good = goodsRepository.getItem(Integer.parseInt(String.join("", requestHead.split("[^\\d-]"))));
        if (good == null) {
            return Map.of(CONFLICT_CODE, jsonMapper().writeValueAsString
                    (Map.of(CONFLICT_CODE, CONFLICT_MESSAGE)));
        } else {
            return Map.of(OK_CODE, jsonMapper().writeValueAsString(good));
        }
    }
}

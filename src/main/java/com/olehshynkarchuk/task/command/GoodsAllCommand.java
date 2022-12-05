package com.olehshynkarchuk.task.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.olehshynkarchuk.task.goods.GoodsRepository;

import java.util.Map;

import static com.olehshynkarchuk.task.constant.Constants.HttpStatusCodeResponse.OK_CODE;

public record GoodsAllCommand(GoodsRepository goodsRepository, JsonMapper jsonMapper) implements Command {
    @Override
    public Map<Integer, String> execute(String requestHead, String requestBody) throws JsonProcessingException {
        return Map.of(OK_CODE, jsonMapper.writeValueAsString(goodsRepository.getAllGoodsTable()));
    }
}

package com.olehshynkarchuk.task.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.olehshynkarchuk.task.repo.Goods;
import com.olehshynkarchuk.task.repo.Repository;

public record CommandNewGoods() implements Command<Integer> {

    @Override
    public Integer execute(String jsonGoodsObject, Repository repository) {
        ObjectMapper objectMapper = new ObjectMapper();
        Goods goods = null;
        try {
            goods = objectMapper.readValue(jsonGoodsObject, Goods.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (goods != null) repository.putItem(goods);
        if (repository.items.containsValue(goods)) return 1;
        else return 0;
    }
}

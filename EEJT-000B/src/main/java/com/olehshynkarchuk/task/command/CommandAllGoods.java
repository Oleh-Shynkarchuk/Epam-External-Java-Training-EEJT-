package com.olehshynkarchuk.task.command;

import com.olehshynkarchuk.task.goods.Goods;
import com.olehshynkarchuk.task.goods.Repository;

import java.util.Map;

public record CommandAllGoods() implements Command<Map<Integer, Goods>> {

    @Override
    public Map<Integer, Goods> execute(String request, Repository repository) {
        return repository.getAllGoodsTable();
    }
}

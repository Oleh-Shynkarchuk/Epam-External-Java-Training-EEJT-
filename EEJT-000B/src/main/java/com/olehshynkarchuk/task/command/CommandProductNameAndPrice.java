package com.olehshynkarchuk.task.command;

import com.olehshynkarchuk.task.goods.Goods;
import com.olehshynkarchuk.task.goods.Repository;

public record CommandProductNameAndPrice() implements Command<Goods> {

    @Override
    public Goods execute(String request, Repository repository) {

        return repository.getItem(Integer.parseInt(request));
    }

}

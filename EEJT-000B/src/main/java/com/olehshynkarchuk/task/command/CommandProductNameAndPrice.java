package com.olehshynkarchuk.task.command;

import com.olehshynkarchuk.task.repo.Goods;
import com.olehshynkarchuk.task.repo.Repository;

public record CommandProductNameAndPrice() implements Command<Goods> {

    @Override
    public Goods execute(String request, Repository repository) {
        return repository.getItem(1);
    }

}

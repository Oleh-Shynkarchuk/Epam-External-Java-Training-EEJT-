package com.olehshynkarchuk.task.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.olehshynkarchuk.task.goods.Repository;

public record CommandAllGoods(Repository repository) implements Command<String> {

    @Override
    public String execute(String request, Repository repository) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(repository.getAllGoodsTable());
    }
}

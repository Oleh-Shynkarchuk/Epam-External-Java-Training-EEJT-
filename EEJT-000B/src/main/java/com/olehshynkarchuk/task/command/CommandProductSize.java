package com.olehshynkarchuk.task.command;


import com.olehshynkarchuk.task.goods.Repository;

import java.util.Map;

public class CommandProductSize implements Command<Map<String, Integer>> {


    @Override
    public Map<String, Integer> execute(String request, Repository repository) {
        return repository.getCount();
    }
}

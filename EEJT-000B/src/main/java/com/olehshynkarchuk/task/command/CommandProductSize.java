package com.olehshynkarchuk.task.command;


import com.olehshynkarchuk.task.repo.Repository;

import java.util.Map;

public class CommandProductSize implements Command<Map<String, Integer>> {


    @Override
    public Map<String, Integer> execute(String request, Repository repository) {
        System.out.println("here " + repository.items.size());
        System.out.println("re " + repository.getCount());
        return repository.getCount();
    }
}

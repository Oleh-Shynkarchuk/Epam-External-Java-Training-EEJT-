package com.olehshynkarchuk.task.command;


import com.olehshynkarchuk.task.goods.Repository;

public interface Command<E> {
    E execute(String request, Repository repository);
}

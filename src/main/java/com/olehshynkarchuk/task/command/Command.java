package com.olehshynkarchuk.task.command;


import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Map;

public interface Command {
    Map<Integer, String> execute(String requestHead, String requestBody) throws JsonProcessingException;
}

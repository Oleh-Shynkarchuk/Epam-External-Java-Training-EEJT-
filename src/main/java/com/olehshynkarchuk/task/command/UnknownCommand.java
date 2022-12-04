package com.olehshynkarchuk.task.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.util.Map;

public record UnknownCommand(JsonMapper jsonMapper) implements Command {
    @Override
    public Map<Integer, String> execute(String requestHead, String requestBody) throws JsonProcessingException {
        return Map.of(404, jsonMapper().writeValueAsString(Map.of(404, "Error:Not Found")));
    }
}

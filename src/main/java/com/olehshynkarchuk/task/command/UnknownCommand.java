package com.olehshynkarchuk.task.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.util.Map;

import static com.olehshynkarchuk.task.constant.Constants.HttpMessageResponse.NF_MESSAGE;
import static com.olehshynkarchuk.task.constant.Constants.HttpStatusCodeResponse.NF_CODE;

public record UnknownCommand(JsonMapper jsonMapper) implements Command {

    @Override
    public Map<Integer, String> execute(String requestHead, String requestBody) throws JsonProcessingException {
        return Map.of(NF_CODE, jsonMapper().writeValueAsString(Map.of(NF_CODE, NF_MESSAGE)));
    }
}

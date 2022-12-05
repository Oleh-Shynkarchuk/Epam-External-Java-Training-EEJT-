package com.olehshynkarchuk.task.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.olehshynkarchuk.task.goods.GoodsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.olehshynkarchuk.task.command.CommandContainer.Command.getCommand;
import static com.olehshynkarchuk.task.constant.Constants.HttpMessageResponse.BAD_REQUEST_MESSAGE;
import static com.olehshynkarchuk.task.constant.Constants.HttpMessageResponse.CONFLICT_MESSAGE;
import static com.olehshynkarchuk.task.constant.Constants.HttpStatusCodeResponse.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class DeleteGoodsCommandTest {
    private CommandContainer container;
    private JsonMapper jsonMapper;
    private GoodsRepository goodsRepository;

    @BeforeEach
    void setUp() {
        jsonMapper = new JsonMapper();
        goodsRepository = new GoodsRepository();
        container = new CommandContainer(goodsRepository, jsonMapper);
    }

    @Test
    void testDelete() throws JsonProcessingException {

        assertEquals(Map.of(OK_CODE, jsonMapper.writeValueAsString(goodsRepository.getItem(1))),
                container.commandList.get(getCommand("DELETE", "/shop/item/delete/1"))
                        .execute("/shop/item/delete/1", ""));
        assertNotEquals(Map.of(OK_CODE, jsonMapper.writeValueAsString(goodsRepository.getItem(1))),
                container.commandList.get(getCommand("DELETE", "/shop/item/delete/1"))
                        .execute("/shop/item/delete/1", ""));
        assertEquals(jsonMapper.writeValueAsString(Map.of(CONFLICT_CODE, CONFLICT_MESSAGE)),
                container.commandList.get(getCommand("DELETE", "/shop/item/delete/1"))
                        .execute("/shop/item/1/delete", "").entrySet().iterator().next().getValue());
    }

    @Test
    void shouldReturnBadRequestWhenDeleteNegativeID() throws JsonProcessingException {
        assertEquals(jsonMapper.writeValueAsString(Map.of(BAD_REQUEST_CODE, BAD_REQUEST_MESSAGE))
                , container.commandList.get(getCommand("DELETE", "/shop/item/delete/-11"))
                        .execute("/shop/item/delete/-11", "").entrySet().iterator().next().getValue());
    }

    @Test
    void shouldReturnBadRequestWhenDeleteNonNumberID() throws JsonProcessingException {
        assertEquals(jsonMapper.writeValueAsString(Map.of(BAD_REQUEST_CODE, BAD_REQUEST_MESSAGE))
                , container.commandList.get(getCommand("DELETE", "/shop/item/delete/-11"))
                        .execute("/shop/item/delete/-11", "").entrySet().iterator().next().getValue());
    }

    @Test
    void shouldReturnConfictWhenDeletePositiveIDButNExist() throws JsonProcessingException {
        assertEquals(jsonMapper.writeValueAsString(Map.of(CONFLICT_CODE, CONFLICT_MESSAGE))
                , container.commandList.get(getCommand("DELETE", "/shop/item/delete/123"))
                        .execute("/shop/item/delete/123", "").entrySet().iterator().next().getValue());
    }
}
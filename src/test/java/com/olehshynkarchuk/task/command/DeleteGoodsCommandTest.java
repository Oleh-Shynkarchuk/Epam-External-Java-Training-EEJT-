package com.olehshynkarchuk.task.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.olehshynkarchuk.task.goods.GoodsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.olehshynkarchuk.task.command.CommandContainer.Command.getCommand;
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

        assertEquals(Map.of(200, jsonMapper.writeValueAsString(goodsRepository.getItem(1))), container.commandList.get(getCommand("DELETE", "/shop/item/1/delete"))
                .execute("/shop/item/1/delete", ""));
        assertNotEquals(Map.of(200, jsonMapper.writeValueAsString(goodsRepository.getItem(1))), container.commandList.get(getCommand("DELETE", "/shop/item/1/delete"))
                .execute("/shop/item/1/delete", ""));
        assertEquals(jsonMapper.writeValueAsString(Map.of(409, "Error:Goods with this ID doesn't exist in the Repository"))
                , container.commandList.get(getCommand("DELETE", "/shop/item/1/delete"))
                        .execute("/shop/item/1/delete", "").entrySet().iterator().next().getValue());
    }

    @Test
    void testDeleteNegativeID() throws JsonProcessingException {
        assertEquals(jsonMapper.writeValueAsString(Map.of(404, "Error:Not Found"))
                , container.commandList.get(getCommand("DELETE", "/shop/item/-1/delete"))
                        .execute("/shop/item/-1/delete", "").entrySet().iterator().next().getValue());
    }

    @Test
    void testDeletePositiveIDButNExist() throws JsonProcessingException {
        assertEquals(jsonMapper.writeValueAsString(Map.of(409, "Error:Goods with this ID doesn't exist in the Repository"))
                , container.commandList.get(getCommand("DELETE", "/shop/item/123/delete"))
                        .execute("/shop/item/123/delete", "").entrySet().iterator().next().getValue());
    }
}
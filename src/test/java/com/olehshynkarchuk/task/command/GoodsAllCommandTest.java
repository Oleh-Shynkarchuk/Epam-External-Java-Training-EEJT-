package com.olehshynkarchuk.task.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.olehshynkarchuk.task.goods.GoodsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.olehshynkarchuk.task.command.CommandContainer.Command.getCommand;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GoodsAllCommandTest {

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
    void testGetAllGoods() throws JsonProcessingException {
        assertEquals(Map.of(200, jsonMapper.writeValueAsString(goodsRepository.getAllGoodsTable()))
                , container.commandList.get(getCommand("GET", "get items"))
                        .execute("", ""));
    }
}
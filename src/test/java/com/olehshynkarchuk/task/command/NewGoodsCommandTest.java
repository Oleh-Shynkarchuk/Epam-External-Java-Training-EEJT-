package com.olehshynkarchuk.task.command;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.olehshynkarchuk.task.goods.GoodsRepository;
import org.junit.jupiter.api.BeforeEach;

class NewGoodsCommandTest {
    private CommandContainer container;
    private JsonMapper jsonMapper;
    private GoodsRepository goodsRepository;

    @BeforeEach
    void setUp() {
        jsonMapper = new JsonMapper();
        goodsRepository = new GoodsRepository();
        container = new CommandContainer(goodsRepository, jsonMapper);
    }

}
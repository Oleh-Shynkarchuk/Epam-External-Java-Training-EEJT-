package com.olehshynkarchuk.task.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.olehshynkarchuk.task.goods.GoodsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoodsSizeCommandTest {

    @Mock
    private GoodsRepository goodsRepository1;
    @Mock
    private JsonMapper jsonMapper1;
    @InjectMocks
    private GoodsSizeCommand goodsSizeCommand;

    @Test
    void shouldReturnAmountOfAllGoodsInRepo() throws JsonProcessingException {

        when(goodsRepository1.getCount()).thenReturn(Map.of("count", 10));
        when(jsonMapper1.writeValueAsString(Map.of("count", 10))).thenReturn("{\"count\":10}");

        assertNotNull(goodsSizeCommand.execute("", ""));
    }
}
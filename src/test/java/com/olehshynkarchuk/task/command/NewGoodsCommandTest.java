package com.olehshynkarchuk.task.command;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.olehshynkarchuk.task.goods.Goods;
import com.olehshynkarchuk.task.goods.GoodsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static com.olehshynkarchuk.task.constant.Constants.HttpMessageResponse.BAD_REQUEST_MESSAGE;
import static com.olehshynkarchuk.task.constant.Constants.HttpMessageResponse.UNPROCESSABLE_ENTITY_MESSAGE;
import static com.olehshynkarchuk.task.constant.Constants.HttpStatusCodeResponse.BAD_REQUEST_CODE;
import static com.olehshynkarchuk.task.constant.Constants.HttpStatusCodeResponse.UNPROCESSABLE_ENTITY_CODE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NewGoodsCommandTest {
    @Mock
    private GoodsRepository goodsRepository;
    @Mock
    private JsonMapper jsonMapper;
    @InjectMocks
    private NewGoodsCommand newGoodsCommand;


    @Test
    void shouldReturnBadRequestIfNewGoodsNull() throws JsonProcessingException {
        //given
        String requestBody = "{}";
        Map<Integer, String> expected = Map.of(BAD_REQUEST_CODE,
                "{" + BAD_REQUEST_CODE + ":\""
                        + BAD_REQUEST_MESSAGE + "\"}");
        //when
        when(jsonMapper.readValue(requestBody, Goods.class))
                .thenReturn(new Goods("", 0.0));
        when(jsonMapper.writeValueAsString(Map.of(BAD_REQUEST_CODE,
                BAD_REQUEST_MESSAGE)))
                .thenReturn("{" + BAD_REQUEST_CODE + ":\"" + BAD_REQUEST_MESSAGE + "\"}");

        //then
        assertEquals(expected, newGoodsCommand.execute("", requestBody));
    }

    @Test
    void shouldReturnUnprocessableEntityIfNewGoodsWhichAddingToRepoHaveZeroPrice() throws JsonProcessingException {
        //given
        String requestBody = "{\"name\":\"nameStub\",\"price\":0.0}";
        Map<Integer, String> expected = Map.of(UNPROCESSABLE_ENTITY_CODE,
                "{" + UNPROCESSABLE_ENTITY_CODE + ":\""
                        + UNPROCESSABLE_ENTITY_MESSAGE + "\"}");
        //when
        when(jsonMapper.readValue(requestBody, Goods.class))
                .thenReturn(new Goods("nameStub", 0.0));
        when(jsonMapper.writeValueAsString(Map.of(UNPROCESSABLE_ENTITY_CODE,
                UNPROCESSABLE_ENTITY_MESSAGE)))
                .thenReturn("{" + UNPROCESSABLE_ENTITY_CODE + ":\"" + UNPROCESSABLE_ENTITY_MESSAGE + "\"}");

        //then
        assertEquals(expected, newGoodsCommand.execute("", requestBody));
    }

    @Test
    void shouldReturnUnprocessableEntityIfNewGoodsWhichAddingToRepoHaveNegativePrice() throws JsonProcessingException {
        //given
        String requestBody = "{\"name\":\"nameStub\",\"price\":-100.34}";
        Map<Integer, String> expected = Map.of(422,
                "{" + UNPROCESSABLE_ENTITY_CODE + ":\"" + UNPROCESSABLE_ENTITY_MESSAGE + "\"}");
        //when
        when(jsonMapper.readValue(requestBody, Goods.class))
                .thenReturn(new Goods("nameStub", -100.34));
        when(jsonMapper.writeValueAsString(Map.of(UNPROCESSABLE_ENTITY_CODE,
                UNPROCESSABLE_ENTITY_MESSAGE)))
                .thenReturn("{" + UNPROCESSABLE_ENTITY_CODE + ":\"" + UNPROCESSABLE_ENTITY_MESSAGE + "\"}");

        //then
        assertEquals(expected, newGoodsCommand.execute("", requestBody));
    }

    @Test
    void shouldReturnBadRequestIfNewGoodsWhichAddingToRepoWhenAtLeastOneGoodsFieldAreMissing() throws JsonProcessingException {
        //given
        String firstRequestBody = "{\"price\":254.31}";
        String secondRequestBody = "{\"name\":\"\",\"price\":254.31}";
        Map<Integer, String> expected = Map.of(400,
                "{400:\"Bad Request\"}");
        //when
        when(jsonMapper.readValue(firstRequestBody, Goods.class))
                .thenReturn(new Goods(null, 254.31));
        when(jsonMapper.readValue(secondRequestBody, Goods.class))
                .thenReturn(new Goods("", 254.31));
        when(jsonMapper.writeValueAsString(Map.of(400,
                "Bad Request")))
                .thenReturn("{400:\"Bad Request\"}");
        //then
        assertEquals(expected, newGoodsCommand.execute("", firstRequestBody));
        assertEquals(expected, newGoodsCommand.execute("", secondRequestBody));
    }

    @Test
    void shouldReturnAddedGoods() throws JsonProcessingException {
        //given
        String firstRequestBody = "{\"name\":\"nameStub\",\"price\":254.31}";
        Goods goods = new Goods("nameStub", 254.31);
        Map<Integer, String> expected = Map.of(200,
                "{\"name\":\"nameStub\",\"price\":254.31}}");

        //when
        when(jsonMapper.readValue(firstRequestBody, Goods.class))
                .thenReturn(goods);
        when(goodsRepository.putItem(goods)).thenReturn(goods);
        when(jsonMapper.writeValueAsString(goods)).thenReturn("{\"name\":\"nameStub\",\"price\":254.31}}");

        //then
        assertEquals(expected, newGoodsCommand.execute("", firstRequestBody));
    }

}
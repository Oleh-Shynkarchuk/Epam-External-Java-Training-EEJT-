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

import static com.olehshynkarchuk.task.constant.Constants.HttpMessageResponse.*;
import static com.olehshynkarchuk.task.constant.Constants.HttpStatusCodeResponse.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReplaceGoodsCommandTest {
    @Mock
    private GoodsRepository goodsRepository;
    @Mock
    private JsonMapper jsonMapper;
    @InjectMocks
    private ReplaceGoodsCommand replaceGoodsCommand;

    @Test
    void shouldUnprocessableIfGoodsWhichAddingToRepoWhenAtLeastOneGoodsFieldAreMissing() throws JsonProcessingException {
        //given
        String requestHead = "/shop/item/put/2";
        String firstRequestBody = "{\"price\":254.31}";
        String secondRequestBody = "{\"name\":\"\",\"price\":254.31}";
        Map<Integer, String> expected = Map.of(UNPROCESSABLE_ENTITY_CODE,
                "{" + UNPROCESSABLE_ENTITY_CODE + ":\"" + UNPROCESSABLE_ENTITY_MESSAGE + "\"}");
        //when
        when(jsonMapper.readValue(firstRequestBody, Goods.class))
                .thenReturn(new Goods(null, 254.31));
        when(jsonMapper.readValue(secondRequestBody, Goods.class))
                .thenReturn(new Goods("", 254.31));
        when(jsonMapper.writeValueAsString(Map.of(UNPROCESSABLE_ENTITY_CODE,
                UNPROCESSABLE_ENTITY_MESSAGE)))
                .thenReturn("{" + UNPROCESSABLE_ENTITY_CODE + ":\"" + UNPROCESSABLE_ENTITY_MESSAGE + "\"}");
        //then
        assertEquals(expected, replaceGoodsCommand.execute(requestHead, firstRequestBody));
        assertEquals(expected, replaceGoodsCommand.execute(requestHead, secondRequestBody));
    }

    @Test
    void shouldReturnBadRequestWhenNonNumericID() throws JsonProcessingException {
        //given
        String requestHead = "/shop/item/put/a";
        Map<Integer, String> expected = Map.of(BAD_REQUEST_CODE,
                "{" + BAD_REQUEST_CODE + ":\"" + BAD_REQUEST_MESSAGE + "\"}");

        //when

        when(jsonMapper.writeValueAsString(Map.of(BAD_REQUEST_CODE,
                BAD_REQUEST_MESSAGE))).thenReturn("{" + BAD_REQUEST_CODE + ":\"" + BAD_REQUEST_MESSAGE + "\"}");

        //then
        assertEquals(expected, replaceGoodsCommand.execute(requestHead, ""));
    }

    @Test
    void shouldReturnUnprocessableEntityIfGoodsWhichAddingToRepoHaveNegativePrice() throws JsonProcessingException {
        //given
        String requestHead = "/shop/item/put/2";
        String requestBody = "{\"name\":\"nameStub\",\"price\":-100.34}";
        Map<Integer, String> expected = Map.of(UNPROCESSABLE_ENTITY_CODE,
                "{" + UNPROCESSABLE_ENTITY_CODE + ":\"" + UNPROCESSABLE_ENTITY_MESSAGE + "\"}");
        //when
        when(jsonMapper.readValue(requestBody, Goods.class))
                .thenReturn(new Goods("nameStub", -100.34));
        when(jsonMapper.writeValueAsString(Map.of(UNPROCESSABLE_ENTITY_CODE,
                UNPROCESSABLE_ENTITY_MESSAGE)))
                .thenReturn("{" + UNPROCESSABLE_ENTITY_CODE + ":\"" + UNPROCESSABLE_ENTITY_MESSAGE + "\"}");

        //then
        assertEquals(expected, replaceGoodsCommand.execute(requestHead, requestBody));
    }

    @Test
    void shouldReturnGoodsWithThatIDDoesNotExistForReplacing() throws JsonProcessingException {
        //given
        String requestHead = "/shop/item/put/2";
        int ID = 2;
        String firstRequestBody = "{\"name\":\"nameStub\",\"price\":254.31}";
        Goods goods = new Goods("nameStub", 254.31);
        Map<Integer, String> expected = Map.of(CONFLICT_CODE,
                "{" + CONFLICT_CODE + ":\"" + CONFLICT_MESSAGE + "\"}");

        //when
        when(jsonMapper.readValue(firstRequestBody, Goods.class))
                .thenReturn(goods);
        when(goodsRepository.replaceGoodsByID(ID, goods)).thenReturn(null);
        when(jsonMapper.writeValueAsString(Map.of(CONFLICT_CODE,
                CONFLICT_MESSAGE))).thenReturn("{" + CONFLICT_CODE + ":\"" + CONFLICT_MESSAGE + "\"}");

        //then
        assertEquals(expected, replaceGoodsCommand.execute(requestHead, firstRequestBody));
    }

    @Test
    void shouldReturnAddedGoods() throws JsonProcessingException {
        //given
        String requestHead = "/shop/item/put/2";
        int ID = 2;
        String firstRequestBody = "{\"name\":\"nameStub\",\"price\":254.31}";
        Goods goods = new Goods("nameStub", 254.31);
        Map<Integer, String> expected = Map.of(OK_CODE,
                "{\"name\":\"nameStub\",\"price\":254.31}}");

        //when
        when(jsonMapper.readValue(firstRequestBody, Goods.class))
                .thenReturn(goods);
        when(goodsRepository.replaceGoodsByID(ID, goods)).thenReturn(goods);
        when(jsonMapper.writeValueAsString(goods)).thenReturn("{\"name\":\"nameStub\",\"price\":254.31}}");

        //then
        assertEquals(expected, replaceGoodsCommand.execute(requestHead, firstRequestBody));
    }

}
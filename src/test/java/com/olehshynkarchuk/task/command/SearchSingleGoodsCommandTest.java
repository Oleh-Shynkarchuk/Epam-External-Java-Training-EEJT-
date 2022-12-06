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
import static com.olehshynkarchuk.task.constant.Constants.HttpMessageResponse.CONFLICT_MESSAGE;
import static com.olehshynkarchuk.task.constant.Constants.HttpStatusCodeResponse.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchSingleGoodsCommandTest {

    @Mock
    private GoodsRepository goodsRepository;
    @Mock
    private JsonMapper jsonMapper;
    @InjectMocks
    private SearchSingleGoodsCommand searchSingleGoodsCommand;

    @Test
    void shouldReturnGoodsWhenDoesNotExist() throws JsonProcessingException {
        //given
        String requestHead = "/shop/item/new/2";
        int ID = 2;
        Map<Integer, String> expected = Map.of(CONFLICT_CODE,
                "{" + CONFLICT_CODE + ":\"" + CONFLICT_MESSAGE + "\"}");

        //when

        when(goodsRepository.getItem(ID)).thenReturn(null);
        when(jsonMapper.writeValueAsString(Map.of(CONFLICT_CODE,
                CONFLICT_MESSAGE))).thenReturn("{" + CONFLICT_CODE + ":\"" + CONFLICT_MESSAGE + "\"}");

        //then
        assertEquals(expected, searchSingleGoodsCommand.execute(requestHead, ""));
    }

    @Test
    void shouldReturnBadRequestWhenNegativeID() throws JsonProcessingException {
        //given
        String requestHead = "/shop/item/new/-2";
        Map<Integer, String> expected = Map.of(BAD_REQUEST_CODE,
                "{" + BAD_REQUEST_CODE + ":\"" + BAD_REQUEST_MESSAGE + "\"}");

        //when
        when(jsonMapper.writeValueAsString(Map.of(BAD_REQUEST_CODE,
                BAD_REQUEST_MESSAGE))).thenReturn("{" + BAD_REQUEST_CODE + ":\"" + BAD_REQUEST_MESSAGE + "\"}");

        //then
        assertEquals(expected, searchSingleGoodsCommand.execute(requestHead, ""));
    }

    @Test
    void shouldReturnBadRequestWhenNonNumericID() throws JsonProcessingException {
        //given
        String requestHead = "/shop/item/new/a";
        Map<Integer, String> expected = Map.of(BAD_REQUEST_CODE,
                "{" + BAD_REQUEST_CODE + ":\"" + BAD_REQUEST_MESSAGE + "\"}");

        //when

        when(jsonMapper.writeValueAsString(Map.of(BAD_REQUEST_CODE,
                BAD_REQUEST_MESSAGE))).thenReturn("{" + BAD_REQUEST_CODE + ":\"" + BAD_REQUEST_MESSAGE + "\"}");

        //then
        assertEquals(expected, searchSingleGoodsCommand.execute(requestHead, ""));
    }

    @Test
    void shouldReturnGoodsWhenExist() throws JsonProcessingException {
        //given
        String requestHead = "/shop/item/new/2";
        int ID = 2;
        Map<Integer, String> expected = Map.of(OK_CODE,
                "{\"name\":\"nameStub\",\"price\":254.31}}");
        Goods goods = new Goods("nameStub", 254.31);
        //when

        when(goodsRepository.getItem(ID)).thenReturn(goods);
        when(jsonMapper.writeValueAsString(goods)).thenReturn("{\"name\":\"nameStub\",\"price\":254.31}}");

        //then
        assertEquals(expected, searchSingleGoodsCommand.execute(requestHead, ""));
    }
}
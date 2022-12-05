package com.olehshynkarchuk.task.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static com.olehshynkarchuk.task.constant.Constants.HttpMessageResponse.NF_MESSAGE;
import static com.olehshynkarchuk.task.constant.Constants.HttpStatusCodeResponse.NF_CODE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UnknownCommandTest {

    @Mock
    private JsonMapper jsonMapper;
    @InjectMocks
    private UnknownCommand unknownCommand;

    @Test
    void testUnknownMessage() throws JsonProcessingException {

        Map<Integer, String> expected = Map.of(NF_CODE,
                "{" + NF_CODE + ":\"" + NF_MESSAGE + "\"}");

        when(jsonMapper.writeValueAsString(Map.of(NF_CODE,
                NF_MESSAGE))).thenReturn("{" + NF_CODE + ":\"" + NF_MESSAGE + "\"}");

        assertEquals(expected, unknownCommand.execute("", ""));
    }
}
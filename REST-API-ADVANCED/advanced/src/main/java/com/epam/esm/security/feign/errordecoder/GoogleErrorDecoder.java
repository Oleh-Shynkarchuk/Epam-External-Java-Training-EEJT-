package com.epam.esm.security.feign.errordecoder;

import com.epam.esm.security.feign.exception.BadRequestException;
import com.epam.esm.security.feign.exception.ExceptionMessage;
import com.fasterxml.jackson.databind.json.JsonMapper;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.IOException;
import java.io.InputStream;

public class GoogleErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        ExceptionMessage message;
        try (InputStream bodyIs = response.body()
                .asInputStream()) {
            message = JsonMapper.builder().build().readValue(bodyIs, ExceptionMessage.class);
        } catch (IOException e) {
            return new Exception(e.getMessage());
        }
        if (response.status() == 400) {
            return new BadRequestException(message.getErrorDescription() != null ? message.getErrorDescription() : "Bad Request");
        }
        return new Default().decode(methodKey, response);
    }
}

package com.epam.esm.security.feign.errordecoder;

import com.epam.esm.security.feign.exception.BadRequestException;
import com.epam.esm.security.feign.exception.ExceptionMessage;
import com.epam.esm.security.feign.exception.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.IOException;
import java.io.InputStream;

public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        ExceptionMessage message;
        try (InputStream bodyIs = response.body()
                .asInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            message = mapper.readValue(bodyIs, ExceptionMessage.class);
        } catch (IOException e) {
            return new Exception(e.getMessage());
        }
        if (response.status() == 400) {
            return new BadRequestException(message.getError_description() != null ? message.getError_description() : "Bad Request");
        }
        if (response.status() == 404) {
            return new NotFoundException(message.getError_description() != null ? message.getError_description() : "Not found");
        }
        return new Default().decode(methodKey, response);
    }
}

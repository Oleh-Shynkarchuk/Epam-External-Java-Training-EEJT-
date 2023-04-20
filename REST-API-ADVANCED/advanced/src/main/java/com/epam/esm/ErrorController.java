package com.epam.esm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@Slf4j
@ControllerAdvice
public class ErrorController extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = ApplicationException.class)
    protected ResponseEntity<Object> handleCertificateInvalidRequestException(ApplicationException ex, WebRequest request) {
        log.debug("Exception handling. {},{}", ex, request);
        log.error("errorCode:" + ex.getErrorCode() + "\n errorMessage:" + ex.getMessage());
        return handleExceptionInternal(ex,
                Map.of("errorCode", ex.getErrorCode(), "errorMessage", ex.getMessage()),
                getHttpHeaders(), ex.getHttpStatus(), request);
    }

    private static HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}

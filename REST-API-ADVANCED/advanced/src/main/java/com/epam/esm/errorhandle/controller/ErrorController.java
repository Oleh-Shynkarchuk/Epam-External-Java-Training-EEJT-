package com.epam.esm.errorhandle.controller;

import com.epam.esm.errorhandle.exception.ApplicationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class ErrorController extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = ApplicationException.class)
    protected ResponseEntity<?> handleCertificateInvalidRequestException(ApplicationException ex, WebRequest request) {
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

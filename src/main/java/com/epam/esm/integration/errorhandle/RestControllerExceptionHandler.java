package com.epam.esm.integration.errorhandle;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice()
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = InvalidRequest.class)
    protected ResponseEntity<?> handleInvalidRequest(InvalidRequest ex, WebRequest request) {
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return handleExceptionInternal(ex,
                Map.of("errorCode",ex.getErrorCode(),"errorMessage",ex.getMessage()),
                httpHeaders, ex.getHttpStatus(), request);
    }
    @ExceptionHandler(value = NoSuchElementException.class)
    protected ResponseEntity<?> handleNotFoundException(NoSuchElementException ex, WebRequest request) {
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return handleExceptionInternal(ex,
                Map.of("errorCode",HttpStatus.NOT_FOUND.value()*100+1,"errorMessage",ex.getMessage()),
                httpHeaders, HttpStatus.NOT_FOUND, request);
    }

}

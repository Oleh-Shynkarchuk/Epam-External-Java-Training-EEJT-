package com.epam.esm.giftcertificates.controller;

import com.epam.esm.giftcertificates.exception.*;
import com.epam.esm.tags.exception.TagInvalidRequestException;
import com.epam.esm.tags.exception.TagNotFoundException;
import com.epam.esm.tags.exception.TagNotRepresentException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@ControllerAdvice()
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = CertificateInvalidRequestException.class)
    protected ResponseEntity<?> handleCertificateInvalidRequestException(CertificateInvalidRequestException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                Map.of("errorCode", ex.getErrorCode(), "errorMessage", ex.getMessage()),
                getHttpHeaders(), ex.getHttpStatus(), request);
    }

    @ExceptionHandler(value = CertificateNotFoundException.class)
    protected ResponseEntity<?> handleCertificateNotFoundException(CertificateNotFoundException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                Map.of("errorCode", ex.getErrorCode(), "errorMessage", ex.getMessage()),
                getHttpHeaders(), ex.getHttpStatus(), request);
    }

    @ExceptionHandler(value = CertificateNotRepresentException.class)
    protected ResponseEntity<?> handleCertificateNotRepresentException(CertificateNotRepresentException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                Map.of("errorCode", ex.getErrorCode(), "errorMessage", ex.getMessage()),
                getHttpHeaders(), ex.getHttpStatus(), request);
    }

    @ExceptionHandler(value = CertificateTransactionException.class)
    protected ResponseEntity<?> handleCertificateTransactionException(CertificateTransactionException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                Map.of("errorCode", ex.getErrorCode(), "errorMessage", ex.getMessage()),
                getHttpHeaders(), ex.getHttpStatus(), request);
    }

    @ExceptionHandler(value = TagInvalidRequestException.class)
    protected ResponseEntity<?> handleTagInvalidRequestException(TagInvalidRequestException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                Map.of("errorCode", ex.getErrorCode(), "errorMessage", ex.getMessage()),
                getHttpHeaders(), ex.getHttpStatus(), request);
    }

    @ExceptionHandler(value = TagNotFoundException.class)
    protected ResponseEntity<?> handleTagNotFoundException(TagNotFoundException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                Map.of("errorCode", ex.getErrorCode(), "errorMessage", ex.getMessage()),
                getHttpHeaders(), ex.getHttpStatus(), request);
    }

    @ExceptionHandler(value = TagNotRepresentException.class)
    protected ResponseEntity<?> handleTagNotRepresentException(TagNotRepresentException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                Map.of("errorCode", ex.getErrorCode(), "errorMessage", ex.getMessage()),
                getHttpHeaders(), ex.getHttpStatus(), request);
    }

    @ExceptionHandler(value = ApplicationException.class)
    protected ResponseEntity<?> handleApplicationException(ApplicationException ex, WebRequest request) {
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

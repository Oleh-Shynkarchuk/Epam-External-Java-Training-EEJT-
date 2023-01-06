package com.epam.esm.integration.errorhandle;

import com.epam.esm.giftcertificates.exception.CertificateNotFound;
import com.epam.esm.giftcertificates.exception.CertificateNotRepresent;
import com.epam.esm.giftcertificates.exception.CertificateInvalidRequest;
import com.epam.esm.giftcertificates.exception.CertificateTransactionException;
import com.epam.esm.tags.exception.TagInvalidRequest;
import com.epam.esm.tags.exception.TagNotFound;
import com.epam.esm.tags.exception.TagNotRepresent;
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
    @ExceptionHandler(value = CertificateInvalidRequest.class)
    protected ResponseEntity<?> handleCertificateInvalidRequestException(CertificateInvalidRequest ex, WebRequest request) {
        return handleExceptionInternal(ex,
                Map.of("errorCode",ex.getErrorCode(),"errorMessage",ex.getMessage()),
                getHttpHeaders(), ex.getHttpStatus(), request);
    }
    @ExceptionHandler(value = CertificateNotFound.class)
    protected ResponseEntity<?> handleCertificateNotFoundException(CertificateNotFound ex, WebRequest request) {
        return handleExceptionInternal(ex,
                Map.of("errorCode",ex.getErrorCode(),"errorMessage",ex.getMessage()),
                getHttpHeaders(), ex.getHttpStatus(), request);
    }
    @ExceptionHandler(value = CertificateNotRepresent.class)
    protected ResponseEntity<?> handleCertificateNotRepresentException(CertificateNotRepresent ex, WebRequest request) {
        return handleExceptionInternal(ex,
                Map.of("errorCode",ex.getErrorCode(),"errorMessage",ex.getMessage()),
                getHttpHeaders(), ex.getHttpStatus(), request);
    }
    @ExceptionHandler(value = CertificateTransactionException.class)
    protected ResponseEntity<?> handleCertificateTransactionException(CertificateTransactionException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                Map.of("errorCode",ex.getErrorCode(),"errorMessage",ex.getMessage()),
                getHttpHeaders(), ex.getHttpStatus(), request);
    }
    @ExceptionHandler(value = TagInvalidRequest.class)
    protected ResponseEntity<?> handleTagInvalidRequestException(TagInvalidRequest ex, WebRequest request) {
        return handleExceptionInternal(ex,
                Map.of("errorCode",ex.getErrorCode(),"errorMessage",ex.getMessage()),
                getHttpHeaders(), ex.getHttpStatus(), request);
    }
    @ExceptionHandler(value = TagNotFound.class)
    protected ResponseEntity<?> handleTagNotFoundException(TagNotFound ex, WebRequest request) {
        return handleExceptionInternal(ex,
                Map.of("errorCode",ex.getErrorCode(),"errorMessage",ex.getMessage()),
                getHttpHeaders(), ex.getHttpStatus(), request);
    }
    @ExceptionHandler(value = TagNotRepresent.class)
    protected ResponseEntity<?> handleTagNotRepresentException(TagNotRepresent ex, WebRequest request) {
        return handleExceptionInternal(ex,
                Map.of("errorCode",ex.getErrorCode(),"errorMessage",ex.getMessage()),
                getHttpHeaders(), ex.getHttpStatus(), request);
    }
    @ExceptionHandler(value = ApplicationException.class)
    protected ResponseEntity<?> handleApplicationException(ApplicationException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                Map.of("errorCode",ex.getErrorCode(),"errorMessage",ex.getMessage()),
                getHttpHeaders(), ex.getHttpStatus(), request);
    }
    private static HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }

}

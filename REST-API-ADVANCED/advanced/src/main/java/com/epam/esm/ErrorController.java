package com.epam.esm;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@Slf4j
@ControllerAdvice
public class ErrorController extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = ApplicationException.class)
    protected ResponseEntity<?> handleCertificateInvalidRequestException(ApplicationException ex, WebRequest request) {
        log.debug("Exception handling.");
        log.error("errorCode:" + ex.getErrorCode() + "\n errorMessage:" + ex.getMessage());
        return handleExceptionInternal(ex,
                Map.of("errorCode", ex.getErrorCode(), "errorMessage", ex.getMessage()),
                getHttpHeaders(), ex.getHttpStatus(), request);
    }

    @ExceptionHandler(value = FeignException.BadRequest.class)
    protected ResponseEntity<?> handleCertificateInvalidRequestException(FeignException.BadRequest ex, WebRequest request) {
        log.debug("Exception handling.");
        log.error("errorCode:" + ex.status() + "\n errorMessage:" + ex.getMessage());
        return handleExceptionInternal(ex,
                Map.of("errorCode", ex.status(), "errorMessage", ex.getMessage()),//todo svoe povidomlenya
                getHttpHeaders(), HttpStatus.valueOf(ex.status()), request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        log.debug("Exception handling.");
        log.error("errorCode:" + 40311 + "\n errorMessage:" + ex.getMessage());
        return handleExceptionInternal(ex,
                Map.of("errorCode", 40311, "errorMessage", ex.getMessage()),
                getHttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<?> handleAccessDeniedException(BadCredentialsException ex, WebRequest request) {
        log.debug("Exception handling.");
        log.error("errorCode:" + 40111 + "\n errorMessage:" + ex.getMessage());
        return handleExceptionInternal(ex,
                Map.of("errorCode", 40111, "errorMessage", ex.getMessage()),
                getHttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    private static HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }

}

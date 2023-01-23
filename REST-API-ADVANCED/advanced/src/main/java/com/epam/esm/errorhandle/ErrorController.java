package com.epam.esm.errorhandle;

import com.epam.esm.InvalidRequest;
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
    @ExceptionHandler(value = InvalidRequest.class)
    protected ResponseEntity<?> handleCertificateInvalidRequestException(InvalidRequest ex, WebRequest request) {
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

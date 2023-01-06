package com.epam.esm.integration.errorhandle;

import org.springframework.http.HttpStatus;

public class ApplicationException extends RuntimeException{
    private final HttpStatus httpStatus=HttpStatus.INTERNAL_SERVER_ERROR;
    private final int errorCode = httpStatus.value()*100+3;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public ApplicationException(String message) {
    super(message);
    }
}

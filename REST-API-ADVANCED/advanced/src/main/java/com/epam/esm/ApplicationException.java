package com.epam.esm;

import org.springframework.http.HttpStatus;

public class ApplicationException extends RuntimeException {

    private final int errorCode;
    private final HttpStatus httpStatus;

    public ApplicationException(String message, int errorCode, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

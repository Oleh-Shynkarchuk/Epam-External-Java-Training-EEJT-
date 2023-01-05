package com.epam.esm.integration.errorhandle;

import org.springframework.http.HttpStatus;

public class ItemNotFound extends ApplicationException {
    private final int errorCode;
    private final HttpStatus httpStatus;

    public ItemNotFound(String message, int errorCode, HttpStatus status) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = status;
    }

    public ItemNotFound(String message) {
        super(message);
        errorCode = 404;
        httpStatus = HttpStatus.NOT_FOUND;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

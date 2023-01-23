package com.epam.esm;

import org.springframework.http.HttpStatus;

public class InvalidRequest extends RuntimeException {
    private final int errorCode = 40010;
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public InvalidRequest(String s) {
        super(s);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

package com.epam.esm.integration.errorhandle;


import org.springframework.http.HttpStatus;

public class InvalidRequest extends RuntimeException{
    private final int errorCode;
    private final HttpStatus httpStatus;
    public InvalidRequest(String message, int errorCode, HttpStatus status) {
        super(message);
        this.errorCode=errorCode;
        this.httpStatus=status;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

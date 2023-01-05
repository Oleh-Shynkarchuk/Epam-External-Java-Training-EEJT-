package com.epam.esm.integration.errorhandle;


import org.springframework.http.HttpStatus;

public class InvalidRequest extends ApplicationException{
    private final int errorCode;
    private final HttpStatus httpStatus;
    public InvalidRequest(String message, int errorCode, HttpStatus status) {
        super(message);
        this.errorCode=errorCode;
        this.httpStatus=status;
    }

    public InvalidRequest(String message) {
        super(message);
        errorCode=400;
        httpStatus=HttpStatus.BAD_REQUEST;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

package com.epam.esm.tags.exception;


import com.epam.esm.integration.errorhandle.ApplicationException;
import org.springframework.http.HttpStatus;

public class TagInvalidRequest extends ApplicationException {
    private final int errorCode=HttpStatus.BAD_REQUEST.value()*100+2;
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    public TagInvalidRequest(String message) {
        super(message);

    }

    public int getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

package com.epam.esm.tags.exception;

import com.epam.esm.integration.errorhandle.ApplicationException;
import org.springframework.http.HttpStatus;

public class TagNotFound extends ApplicationException {
    private final int errorCode=HttpStatus.NOT_FOUND.value()*100+2;
    private final HttpStatus httpStatus=HttpStatus.NOT_FOUND;

    public TagNotFound(String message) {
        super(message);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

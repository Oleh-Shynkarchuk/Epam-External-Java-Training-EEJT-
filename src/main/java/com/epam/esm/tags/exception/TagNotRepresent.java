package com.epam.esm.tags.exception;

import com.epam.esm.integration.errorhandle.ApplicationException;
import org.springframework.http.HttpStatus;

public class TagNotRepresent extends ApplicationException {
    private final int errorCode = HttpStatus.INTERNAL_SERVER_ERROR.value()*100+2;
    private final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    public TagNotRepresent(String s) {
        super(s);
    }
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public int getErrorCode() {
        return errorCode;
    }
}

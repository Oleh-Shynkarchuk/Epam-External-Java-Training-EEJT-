package com.epam.esm.tags.exception;

import com.epam.esm.giftcertificates.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class TagNotRepresentException extends ApplicationException {

    private final int errorCode = ErrorConstants.TAG_NOT_REPRESENT_EXCEPTION_ERROR_CODE;
    private final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    public TagNotRepresentException(String s) {
        super(s);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

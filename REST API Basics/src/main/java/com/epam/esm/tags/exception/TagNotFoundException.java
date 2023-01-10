package com.epam.esm.tags.exception;

import com.epam.esm.giftcertificates.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class TagNotFoundException extends ApplicationException {

    private final int errorCode = ErrorConstants.TAG_NOT_FOUND_EXCEPTION_ERROR_CODE;
    private final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    public TagNotFoundException(String message) {
        super(message);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

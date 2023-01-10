package com.epam.esm.tags.exception;

import com.epam.esm.giftcertificates.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class TagInvalidRequestException extends ApplicationException {

    private final int errorCode = ErrorConstants.TAG_INVALID_REQUEST_ERROR_CODE;
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public TagInvalidRequestException(String message) {
        super(message);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

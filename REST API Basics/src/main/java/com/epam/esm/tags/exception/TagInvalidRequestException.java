package com.epam.esm.tags.exception;

import com.epam.esm.giftcertificates.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class TagInvalidRequestException extends ApplicationException {
    public TagInvalidRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST, ErrorConstants.TAG_INVALID_REQUEST_ERROR_CODE);
    }
}

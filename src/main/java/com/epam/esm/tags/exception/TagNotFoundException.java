package com.epam.esm.tags.exception;

import com.epam.esm.giftcertificates.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class TagNotFoundException extends ApplicationException {
    public TagNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, ErrorConstants.TAG_NOT_FOUND_EXCEPTION_ERROR_CODE);
    }
}

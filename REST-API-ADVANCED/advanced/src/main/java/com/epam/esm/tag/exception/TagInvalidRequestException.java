package com.epam.esm.tag.exception;

import com.epam.esm.ApplicationException;
import com.epam.esm.ErrorConstants;
import org.springframework.http.HttpStatus;

public class TagInvalidRequestException extends ApplicationException {
    public TagInvalidRequestException(String message) {
        super(message, ErrorConstants.TAG_INVALID_REQUEST_ERROR_CODE, HttpStatus.BAD_REQUEST);
    }
}
package com.epam.esm.tag.exception;

import com.epam.esm.errorhandle.constants.ErrorConstants;
import com.epam.esm.errorhandle.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class TagNotFoundException extends ApplicationException {
    public TagNotFoundException(String message) {
        super(message, ErrorConstants.TAG_NOT_FOUND_ERROR_CODE, HttpStatus.BAD_REQUEST);
    }
}
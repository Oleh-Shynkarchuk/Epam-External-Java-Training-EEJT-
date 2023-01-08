package com.epam.esm.tags.exception;

import com.epam.esm.giftcertificates.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class TagNotRepresentException extends ApplicationException {
    public TagNotRepresentException(String s) {
        super(s, HttpStatus.INTERNAL_SERVER_ERROR, ErrorConstants.TAG_NOT_REPRESENT_EXCEPTION_ERROR_CODE);
    }
}

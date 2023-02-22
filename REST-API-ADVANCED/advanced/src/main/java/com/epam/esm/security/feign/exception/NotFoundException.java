package com.epam.esm.security.feign.exception;

import com.epam.esm.ApplicationException;
import com.epam.esm.ErrorConstants;
import org.springframework.http.HttpStatus;

public class NotFoundException extends ApplicationException {
    public NotFoundException(String message) {
        super(message, ErrorConstants.SECURITY_NOT_FOUND, HttpStatus.BAD_REQUEST);
    }
}

package com.epam.esm.security.feign.exception;

import com.epam.esm.ApplicationException;
import com.epam.esm.ErrorConstants;
import org.springframework.http.HttpStatus;

public class BadRequestException extends ApplicationException {
    public BadRequestException(String message) {
        super(message, ErrorConstants.SECURITY_INVALID_TOKEN, HttpStatus.BAD_REQUEST);
    }
}

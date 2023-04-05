package com.epam.esm.security.exception;

import com.epam.esm.ApplicationException;
import com.epam.esm.ErrorConstants;
import org.springframework.http.HttpStatus;

public class KeyPairException extends ApplicationException {
    public KeyPairException(String message) {
        super(message, ErrorConstants.KEY_PAIR_CODE, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

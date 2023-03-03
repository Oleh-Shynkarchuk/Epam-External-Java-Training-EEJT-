package com.epam.esm.security.exception;

import com.epam.esm.ApplicationException;
import com.epam.esm.ErrorConstants;
import org.springframework.http.HttpStatus;

public class TokenBadRequestException extends ApplicationException {
    public TokenBadRequestException() {
        super(
                ErrorConstants.EMPTY_REFRESH_TOKEN_MESSAGE,
                ErrorConstants.SECURITY_INVALID_TOKEN,
                HttpStatus.BAD_REQUEST
        );
    }
}

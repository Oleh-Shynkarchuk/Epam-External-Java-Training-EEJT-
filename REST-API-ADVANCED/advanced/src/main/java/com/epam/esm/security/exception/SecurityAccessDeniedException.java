package com.epam.esm.security.exception;

import com.epam.esm.ApplicationException;
import com.epam.esm.ErrorConstants;
import org.springframework.http.HttpStatus;

public class SecurityAccessDeniedException extends ApplicationException {
    public SecurityAccessDeniedException(String message) {
        super(message, ErrorConstants.SECURITY_ACCESS_DENIED, HttpStatus.FORBIDDEN);
    }
}

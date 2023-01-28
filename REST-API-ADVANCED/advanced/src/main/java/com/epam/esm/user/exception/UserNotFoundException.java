package com.epam.esm.user.exception;

import com.epam.esm.errorhandle.constants.ErrorConstants;
import com.epam.esm.errorhandle.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ApplicationException {
    public UserNotFoundException(String message) {
        super(message, ErrorConstants.USER_NOT_FOUND_ERROR_CODE, HttpStatus.NOT_FOUND);
    }
}

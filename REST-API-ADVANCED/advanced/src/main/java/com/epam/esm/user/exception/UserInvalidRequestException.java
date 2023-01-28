package com.epam.esm.user.exception;

import com.epam.esm.errorhandle.constants.ErrorConstants;
import com.epam.esm.errorhandle.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class UserInvalidRequestException extends ApplicationException {
    public UserInvalidRequestException(String message) {
        super(message, ErrorConstants.USER_INVALID_REQUEST_ERROR_CODE, HttpStatus.BAD_REQUEST);
    }
}
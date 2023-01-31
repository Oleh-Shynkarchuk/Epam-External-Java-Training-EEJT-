package com.epam.esm.order.exception;

import com.epam.esm.ApplicationException;
import com.epam.esm.ErrorConstants;
import org.springframework.http.HttpStatus;

public class OrderInvalidRequestException extends ApplicationException {
    public OrderInvalidRequestException(String message) {
        super(message, ErrorConstants.ORDER_INVALID_REQUEST_ERROR_CODE, HttpStatus.BAD_REQUEST);
    }
}
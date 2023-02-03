package com.epam.esm.order.exception;

import com.epam.esm.ApplicationException;
import com.epam.esm.ErrorConstants;
import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends ApplicationException {
    public OrderNotFoundException() {
        super(ErrorConstants.ORDER_NOT_FOUND_MESSAGE, ErrorConstants.ORDER_NOT_FOUND_ERROR_CODE, HttpStatus.NOT_FOUND);
    }
}
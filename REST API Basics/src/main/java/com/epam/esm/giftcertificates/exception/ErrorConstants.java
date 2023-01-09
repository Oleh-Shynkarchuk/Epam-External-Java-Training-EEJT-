package com.epam.esm.giftcertificates.exception;

import org.springframework.http.HttpStatus;

public class ErrorConstants {
    public static final int CERTIFICATE_NOT_REPRESENT_EXCEPTION_ERROR_CODE = HttpStatus.INTERNAL_SERVER_ERROR.value() * 100 + 1;
    public static final int INVALID_REQUEST_ERROR_CODE = HttpStatus.BAD_REQUEST.value() * 100 + 1;
    public static final int CERTIFICATE_NOT_FOUND_EXCEPTION_ERROR_CODE = HttpStatus.NOT_FOUND.value() * 100 + 1;
    public static final int CERTIFICATE_TRANSACTION_EXCEPTION_ERROR_CODE = HttpStatus.INTERNAL_SERVER_ERROR.value() * 100 + 1;

}

package com.epam.esm.giftcertificates.exception;

import org.springframework.http.HttpStatus;

public class ApplicationException extends RuntimeException {
    private final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    private final int errorCode = ErrorConstants.CERTIFICATE_APPLICATION_EXCEPTION_ERROR_CODE;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public ApplicationException(String message) {
        super(message);
    }
}

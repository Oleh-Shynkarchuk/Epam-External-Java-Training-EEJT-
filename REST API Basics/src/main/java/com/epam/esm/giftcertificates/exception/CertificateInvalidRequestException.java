package com.epam.esm.giftcertificates.exception;

import org.springframework.http.HttpStatus;

public class CertificateInvalidRequestException extends ApplicationException {

    private final int errorCode = ErrorConstants.INVALID_REQUEST_ERROR_CODE;
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public CertificateInvalidRequestException(String message) {
        super(message);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

package com.epam.esm.giftcertificates.exception;

import org.springframework.http.HttpStatus;

public class CertificateNotFoundException extends ApplicationException {

    private final int errorCode = ErrorConstants.CERTIFICATE_NOT_FOUND_EXCEPTION_ERROR_CODE;
    private final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    public CertificateNotFoundException(String message) {
        super(message);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

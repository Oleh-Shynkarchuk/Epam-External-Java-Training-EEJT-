package com.epam.esm.giftcertificates.exception;

import org.springframework.http.HttpStatus;

public class CertificateNotRepresentException extends ApplicationException {

    private final int errorCode = ErrorConstants.CERTIFICATE_NOT_REPRESENT_EXCEPTION_ERROR_CODE;
    private final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    public CertificateNotRepresentException(String s) {
        super(s);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

package com.epam.esm.giftcertificates.exception;

import org.springframework.http.HttpStatus;

public class CertificateTransactionException extends ApplicationException {

    private final int errorCode = ErrorConstants.CERTIFICATE_TRANSACTION_EXCEPTION_ERROR_CODE;
    private final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    public CertificateTransactionException(String s) {
        super(s);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

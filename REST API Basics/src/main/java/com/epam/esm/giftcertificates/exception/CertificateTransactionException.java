package com.epam.esm.giftcertificates.exception;

import org.springframework.http.HttpStatus;

public class CertificateTransactionException extends ApplicationException {
    public CertificateTransactionException(String s) {
        super(s, HttpStatus.INTERNAL_SERVER_ERROR, ErrorConstants.CERTIFICATE_TRANSACTION_EXCEPTION_ERROR_CODE);
    }
}

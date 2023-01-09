package com.epam.esm.giftcertificates.exception;

import org.springframework.http.HttpStatus;

public class CertificateNotFoundException extends ApplicationException {
    public CertificateNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, ErrorConstants.CERTIFICATE_NOT_FOUND_EXCEPTION_ERROR_CODE);
    }
}

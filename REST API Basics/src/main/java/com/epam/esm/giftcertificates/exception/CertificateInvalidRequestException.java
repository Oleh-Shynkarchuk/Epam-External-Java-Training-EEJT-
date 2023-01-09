package com.epam.esm.giftcertificates.exception;

import org.springframework.http.HttpStatus;

public class CertificateInvalidRequestException extends ApplicationException {
    public CertificateInvalidRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST, ErrorConstants.INVALID_REQUEST_ERROR_CODE);
    }
}

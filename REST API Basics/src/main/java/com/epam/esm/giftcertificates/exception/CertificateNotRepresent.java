package com.epam.esm.giftcertificates.exception;

import org.springframework.http.HttpStatus;

public class CertificateNotRepresent extends ApplicationException {
    public CertificateNotRepresent(String s) {
        super(s, HttpStatus.INTERNAL_SERVER_ERROR, ErrorConstants.CERTIFICATE_NOT_REPRESENT_EXCEPTION_ERROR_CODE);
    }
}

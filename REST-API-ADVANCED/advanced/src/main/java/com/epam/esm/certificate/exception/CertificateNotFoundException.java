package com.epam.esm.certificate.exception;

import com.epam.esm.errorhandle.constants.ErrorConstants;
import com.epam.esm.errorhandle.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class CertificateNotFoundException extends ApplicationException {
    public CertificateNotFoundException(String message) {
        super(message, ErrorConstants.CERTIFICATE_NOT_FOUND_ERROR_CODE, HttpStatus.NOT_FOUND);
    }
}

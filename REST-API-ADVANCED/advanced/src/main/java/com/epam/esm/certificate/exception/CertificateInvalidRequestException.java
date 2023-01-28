package com.epam.esm.certificate.exception;

import com.epam.esm.errorhandle.constants.ErrorConstants;
import com.epam.esm.errorhandle.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class CertificateInvalidRequestException extends ApplicationException {
    public CertificateInvalidRequestException(String message) {
        super(message, ErrorConstants.CERTIFICATE_INVALID_REQUEST_ERROR_CODE, HttpStatus.BAD_REQUEST);
    }
}
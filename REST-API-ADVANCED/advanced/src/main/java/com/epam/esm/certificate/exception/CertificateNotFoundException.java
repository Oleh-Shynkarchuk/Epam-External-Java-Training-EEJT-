package com.epam.esm.certificate.exception;

import com.epam.esm.errorhandle.constants.ErrorConstants;
import com.epam.esm.errorhandle.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class CertificateNotFoundException extends ApplicationException {
    public CertificateNotFoundException() {
        super(ErrorConstants.CERTIFICATE_NOT_FOUND_MESSAGE, ErrorConstants.CERTIFICATE_NOT_FOUND_ERROR_CODE, HttpStatus.NOT_FOUND);
    }
}

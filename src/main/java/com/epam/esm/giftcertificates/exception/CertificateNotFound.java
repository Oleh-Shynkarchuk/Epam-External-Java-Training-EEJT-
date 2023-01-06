package com.epam.esm.giftcertificates.exception;

import com.epam.esm.integration.errorhandle.ApplicationException;
import org.springframework.http.HttpStatus;

public class CertificateNotFound extends ApplicationException {
    private final int errorCode=HttpStatus.NOT_FOUND.value()*100+1;
    private final HttpStatus httpStatus=HttpStatus.NOT_FOUND;

    public CertificateNotFound(String message) {
        super(message);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

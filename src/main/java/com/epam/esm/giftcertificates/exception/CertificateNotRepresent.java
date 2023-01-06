package com.epam.esm.giftcertificates.exception;

import com.epam.esm.integration.errorhandle.ApplicationException;
import org.springframework.http.HttpStatus;

public class CertificateNotRepresent extends ApplicationException {
    private final int errorCode = HttpStatus.INTERNAL_SERVER_ERROR.value()*100+1;
    private final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    public CertificateNotRepresent(String s) {
        super(s);
    }
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public int getErrorCode() {
        return errorCode;
    }
}

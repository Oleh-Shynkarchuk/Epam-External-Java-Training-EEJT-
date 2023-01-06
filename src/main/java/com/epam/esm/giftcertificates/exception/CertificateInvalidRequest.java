package com.epam.esm.giftcertificates.exception;


import com.epam.esm.integration.errorhandle.ApplicationException;
import org.springframework.http.HttpStatus;

public class CertificateInvalidRequest extends ApplicationException {
    private final int errorCode;
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    public CertificateInvalidRequest(String message, int errorCode) {
        super(message);
        this.errorCode=errorCode;
    }

    public CertificateInvalidRequest(String message) {
        super(message);
        errorCode=400;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

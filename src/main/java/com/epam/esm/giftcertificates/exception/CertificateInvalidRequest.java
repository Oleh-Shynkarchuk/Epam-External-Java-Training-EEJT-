package com.epam.esm.giftcertificates.exception;


import com.epam.esm.integration.errorhandle.ApplicationException;
import org.springframework.http.HttpStatus;

public class CertificateInvalidRequest extends ApplicationException {
    private final int errorCode=HttpStatus.BAD_REQUEST.value()*100+1;
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    public CertificateInvalidRequest(String message) {
        super(message);
    }
    public int getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

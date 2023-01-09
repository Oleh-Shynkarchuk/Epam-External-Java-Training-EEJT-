package com.epam.esm.tags.exception;

import org.springframework.http.HttpStatus;

public class ErrorConstants {
    public static final int TAG_NOT_REPRESENT_EXCEPTION_ERROR_CODE = HttpStatus.INTERNAL_SERVER_ERROR.value() * 100 + 2;
    public static final int TAG_INVALID_REQUEST_ERROR_CODE = HttpStatus.BAD_REQUEST.value() * 100 + 2;
    public static final int TAG_NOT_FOUND_EXCEPTION_ERROR_CODE = HttpStatus.NOT_FOUND.value() * 100 + 2;
}

package com.epam.esm;

@Generated
public class ErrorConstants {
    private ErrorConstants() {
    }

    public static final int USER_INVALID_REQUEST_ERROR_CODE = 40011;
    public static final int ORDER_INVALID_REQUEST_ERROR_CODE = 40022;
    public static final int CERTIFICATE_INVALID_REQUEST_ERROR_CODE = 40033;
    public static final int TAG_INVALID_REQUEST_ERROR_CODE = 40044;
    public static final int SECURITY_INVALID_TOKEN = 40055;

    public static final int SECURITY_ACCESS_DENIED = 40355;

    public static final int USER_NOT_FOUND_ERROR_CODE = 40411;
    public static final int ORDER_NOT_FOUND_ERROR_CODE = 40422;
    public static final int CERTIFICATE_NOT_FOUND_ERROR_CODE = 40433;
    public static final int TAG_NOT_FOUND_ERROR_CODE = 40444;

    public static final int KEY_PAIR_CODE = 50055;

    public static final String USER_NOT_FOUND_MESSAGE = "None user was found for specified parameters.";
    public static final String ORDER_NOT_FOUND_MESSAGE = "None order was found for specified parameters.";
    public static final String CERTIFICATE_NOT_FOUND_MESSAGE = "None certificate was found for specified parameters.";
    public static final String TAG_NOT_FOUND_MESSAGE = "None tag was found for specified parameters.";
    public static final String EMPTY_REFRESH_TOKEN_MESSAGE = "Refresh token can not be blank";
}

package com.olehshynkarchuk.task.constant;

public class Constants {
    public static class HttpStatusCodeResponse {

        public static final int OK_CODE = 200;

        public static final int BAD_REQUEST_CODE = 400;
        public static final int NF_CODE = 404;
        public static final int CONFLICT_CODE = 409;
        public static final int UNPROCESSABLE_ENTITY_CODE = 422;
    }

    public static class HttpMessageResponse {

        public static final String BAD_REQUEST_MESSAGE = "Bad Request";
        public static final String NF_MESSAGE = "Not Found";
        public static final String CONFLICT_MESSAGE = "Conflict : Goods with this ID doesn't exist in the Repository";
        public static final String UNPROCESSABLE_ENTITY_MESSAGE = "Unprocessable Entity : Price of Goods cannot be negative or zero";
    }
}

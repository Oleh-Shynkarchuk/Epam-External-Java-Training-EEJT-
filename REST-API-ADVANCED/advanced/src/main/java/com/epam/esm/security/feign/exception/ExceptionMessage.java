package com.epam.esm.security.feign.exception;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExceptionMessage {
    @JsonProperty(value = "error_description")
    private String errorDescription;

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }
}
package com.epam.esm.security.feign.tokenmodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VerifiedTokenModel {
    private String iss;
    private String azp;
    private String email;
    @JsonProperty(value = "email_verified")
    private Boolean emailVerified;
    @JsonProperty(value = "at_hash")
    private String atHash;
    private String name;
    private long iat;
    private long exp;
    private String typ;
}

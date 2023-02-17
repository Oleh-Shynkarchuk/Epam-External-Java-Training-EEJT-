package com.epam.esm.security.feign.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TokenVerifiedDTO {
    private String iss;
    private String azp;
    private String email;
    private Boolean email_verified;
    private String at_hash;
    private String name;
    private long iat;
    private long exp;
    private String typ;
}

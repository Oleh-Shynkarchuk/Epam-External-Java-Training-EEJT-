package com.epam.esm.security.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OpenIdConnectionRequest {
    private String accessToken;
    private String idToken;
}

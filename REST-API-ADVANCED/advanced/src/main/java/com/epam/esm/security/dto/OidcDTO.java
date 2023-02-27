package com.epam.esm.security.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OidcDTO {
    private String accessToken;
    private String idToken;
}

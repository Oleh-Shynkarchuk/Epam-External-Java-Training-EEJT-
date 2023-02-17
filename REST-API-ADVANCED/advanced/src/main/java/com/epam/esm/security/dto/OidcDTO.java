package com.epam.esm.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OidcDTO {
    private String accessToken;
    private String idToken;
}

package com.epam.esm.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenModel {
    private String userId;
    private String accessToken;
    private String refreshToken;
}

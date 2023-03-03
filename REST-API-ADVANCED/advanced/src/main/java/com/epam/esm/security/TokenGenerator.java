package com.epam.esm.security;

import com.epam.esm.security.model.TokenModel;
import com.epam.esm.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static com.epam.esm.security.ConstantsUtil.*;

@Component
public class TokenGenerator {

    private final JwtEncoder accessTokenEncoder;
    private final JwtEncoder refreshTokenEncoder;

    @Autowired
    public TokenGenerator(JwtEncoder accessTokenEncoder, JwtEncoder refreshTokenEncoder) {
        this.accessTokenEncoder = accessTokenEncoder;
        this.refreshTokenEncoder = refreshTokenEncoder;
    }

    private String createAccessToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Instant now = Instant.now();
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuer(MY_APP)
                .issuedAt(now)
                .expiresAt(now.plus(5, ChronoUnit.MINUTES))
                .subject(String.valueOf(user.getId()))
                .claim(ROLE, user.getRole())
                .claim(PROVIDER, user.getProvider())
                .build();
        return accessTokenEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
    }

    private String createRefreshToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Instant now = Instant.now();
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuer(MY_APP)
                .issuedAt(now)
                .expiresAt(now.plus(30, ChronoUnit.DAYS))
                .subject(String.valueOf(user.getId()))
                .claim(ROLE, user.getRole())
                .claim(PROVIDER, user.getProvider())
                .build();
        return refreshTokenEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
    }

    public TokenModel createToken(Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof User user)) {
            throw new BadCredentialsException(
                    MessageFormat.format("principal {0} is not of User entity",
                            authentication.getPrincipal().getClass())
            );
        }
        TokenModel tokenModel = new TokenModel();
        tokenModel.setUserId(String.valueOf(user.getId()));
        tokenModel.setAccessToken(createAccessToken(authentication));
        tokenModel.setRefreshToken(createRefreshToken(authentication));
        return tokenModel;
    }
}

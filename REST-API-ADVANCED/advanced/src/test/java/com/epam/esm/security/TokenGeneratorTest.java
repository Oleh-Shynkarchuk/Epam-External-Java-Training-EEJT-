package com.epam.esm.security;

import com.epam.esm.order.entity.Order;
import com.epam.esm.security.dto.TokenDTO;
import com.epam.esm.user.entity.User;
import com.epam.esm.user.entity.provider.Provider;
import com.epam.esm.user.entity.roles.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TokenGeneratorTest {

    private final TokenGenerator tokenGenerator;
    private final JwtDecoder accessTokenDecoder;
    private final JwtDecoder refreshTokenDecoder;

    @Autowired
    TokenGeneratorTest(TokenGenerator tokenGenerator, JwtDecoder accessTokenDecoder, JwtDecoder refreshTokenDecoder) {
        this.tokenGenerator = tokenGenerator;
        this.accessTokenDecoder = accessTokenDecoder;
        this.refreshTokenDecoder = refreshTokenDecoder;
    }

    @Test
    void createToken() {
        User user = User.builder().id(1L).email("test@email.com")
                .password("TestPassword").role(Role.USER).provider(Provider.SELF).build();
        Authentication authentication = UsernamePasswordAuthenticationToken.
                authenticated(user, user.getPassword(), user.getAuthorities());
        TokenDTO token = tokenGenerator.createToken(authentication);
        String issuer = "myApp";
        Jwt decodedAccess = accessTokenDecoder.decode(token.getAccessToken());
        Jwt decodedRefresh = refreshTokenDecoder.decode(token.getRefreshToken());

        assertAll(
                () -> assertEquals(user.getId(), Long.parseLong(token.getUserId())),
                () -> assertEquals(issuer, decodedAccess.getClaimAsString("iss")),
                () -> assertEquals(issuer, decodedRefresh.getClaimAsString("iss")),
                () -> assertEquals(user.getRole().name(), decodedAccess.getClaimAsString("role")),
                () -> assertEquals(user.getRole().name(), decodedRefresh.getClaimAsString("role")),
                () -> assertEquals(user.getProvider().name(), decodedAccess.getClaimAsString("provider")),
                () -> assertEquals(user.getProvider().name(), decodedRefresh.getClaimAsString("provider")),
                () -> {
                    if (decodedAccess.getIssuedAt() != null) {
                        assertEquals(Duration.ofMinutes(5), Duration.between(decodedAccess.getIssuedAt(), decodedAccess.getExpiresAt()));
                    }
                },
                () -> {
                    if (decodedRefresh.getIssuedAt() != null) {
                        assertEquals(Duration.ofDays(30), Duration.between(decodedRefresh.getIssuedAt(), decodedRefresh.getExpiresAt()));
                    }
                }
        );
    }

    @Test
    void createTokenThrowBadCredentials() {
        assertThrows(BadCredentialsException.class, () -> tokenGenerator.createToken(
                UsernamePasswordAuthenticationToken.unauthenticated(Order.builder().build(), null)));
    }
}
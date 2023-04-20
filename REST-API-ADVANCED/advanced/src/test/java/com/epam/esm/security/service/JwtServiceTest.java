package com.epam.esm.security.service;

import com.epam.esm.security.TokenGenerator;
import com.epam.esm.security.exception.TokenBadRequestException;
import com.epam.esm.security.feign.client.GoogleAuthApiClient;
import com.epam.esm.security.feign.model.VerifiedTokenResponce;
import com.epam.esm.security.model.AuthUserRequest;
import com.epam.esm.security.model.OpenIdConnectionRequest;
import com.epam.esm.security.model.TokenModel;
import com.epam.esm.user.entity.User;
import com.epam.esm.user.entity.provider.Provider;
import com.epam.esm.user.entity.roles.Role;
import com.epam.esm.user.exception.UserInvalidRequestException;
import com.epam.esm.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @Mock
    private TokenGenerator tokenGenerator;
    @Mock
    private DaoAuthenticationProvider daoAuthenticationProvider;
    @Mock
    private GoogleAuthApiClient googleAuthApiClient;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private JwtService jwtService;

    @Test
    void register() {
        AuthUserRequest signupUser = new AuthUserRequest("TestUser", "TestPassword");
        User user = User.builder().email(signupUser.getUsername()).password(signupUser.getPassword())
                .role(Role.USER).provider(Provider.BASIC).build();
        TokenModel expected = new TokenModel("1", "accessToken", "refreshToken");
        UsernamePasswordAuthenticationToken authenticated = UsernamePasswordAuthenticationToken.
                authenticated(user, signupUser.getPassword(), user.getAuthorities());

        when(passwordEncoder.encode(user.getPassword())).thenReturn(user.getPassword());
        when(userRepository.existsByEmail(user.getUsername())).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);
        when(tokenGenerator.createToken(authenticated)).thenReturn(expected);


        assertEquals(expected, jwtService.register(signupUser));
    }

    @Test
    void registerThrowUserInvalidRequestExceptionCuzUserExist() {
        AuthUserRequest signupUser = new AuthUserRequest("TestUser", "TestPassword");
        User user = User.builder().email(signupUser.getUsername()).password(signupUser.getPassword())
                .role(Role.USER).provider(Provider.BASIC).build();
        when(userRepository.existsByEmail(user.getUsername())).thenReturn(true);

        assertThrows(UserInvalidRequestException.class, () -> jwtService.register(signupUser));
    }

    @Test
    void login() {
        AuthUserRequest loginUser = new AuthUserRequest("TestUser", "TestPassword");
        User user = User.builder().email(loginUser.getUsername()).password(loginUser.getPassword())
                .role(Role.USER).provider(Provider.BASIC).build();
        Authentication unauthenticated = UsernamePasswordAuthenticationToken.
                unauthenticated(loginUser.getUsername(), loginUser.getPassword());
        Authentication authenticated = UsernamePasswordAuthenticationToken.
                authenticated(user, loginUser.getPassword(), user.getAuthorities());
        TokenModel expected = new TokenModel("1", "accessToken", "refreshToken");

        when(daoAuthenticationProvider.authenticate(unauthenticated)).
                thenReturn(authenticated);
        when(tokenGenerator.createToken(authenticated)).thenReturn(expected);

        assertEquals(expected, jwtService.login(loginUser));
    }

    @Test
    void loginThrowUserInvalidRequestExceptionCuzUserDoesNotExist() {
        AuthUserRequest loginUser = new AuthUserRequest("TestUser", "TestPassword");
        Authentication authentication = UsernamePasswordAuthenticationToken.
                unauthenticated(loginUser.getUsername(), loginUser.getPassword());

        when(daoAuthenticationProvider.authenticate(authentication)).thenThrow(UsernameNotFoundException.class);

        assertThrows(UsernameNotFoundException.class, () -> jwtService.login(loginUser));
    }

    @Test
    void refreshThrowTokenBadRequestExceptionCuzEmptyToken() {
        assertThrows(TokenBadRequestException.class, () -> jwtService.refresh(new TokenModel()));
    }

    @Test
    void oidcUpdateProviderToGoogle() {
        String idToken = "TestOidcToken";
        User user = User.builder().email("TestUser").password("TestPassword")
                .role(Role.USER).provider(Provider.BASIC).build();
        User beforeSave = User.builder().email("TestUser").password("TestPassword")
                .role(Role.USER).provider(Provider.GOOGLE).build();
        User afterSave = User.builder().id("1").email("TestUser").password("TestPassword")
                .role(Role.USER).provider(Provider.GOOGLE).build();
        Authentication authentication = UsernamePasswordAuthenticationToken.
                authenticated(afterSave, null, afterSave.getAuthorities());
        VerifiedTokenResponce verifiedDTO = VerifiedTokenResponce.builder().email(user.getEmail()).build();
        TokenModel expected = new TokenModel("1", "accessToken", "refreshToken");

        when(googleAuthApiClient.getVerifiedToken(idToken)).thenReturn(verifiedDTO);
        when(userRepository.findByEmail(verifiedDTO.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.save(beforeSave)).thenReturn(afterSave);
        when(tokenGenerator.createToken(authentication)).thenReturn(expected);
        assertEquals(expected, jwtService.oidc(OpenIdConnectionRequest.builder().idToken(idToken).build()));
    }

    @Test
    void oidcUsedExistingUser() {
        String idToken = "TestOidcToken";
        User user = User.builder().email("TestUser").password("TestPassword")
                .role(Role.USER).provider(Provider.GOOGLE).build();
        User exist = User.builder().id("1").email("TestUser").password("TestPassword")
                .role(Role.USER).provider(Provider.GOOGLE).build();
        Authentication authentication = UsernamePasswordAuthenticationToken.
                authenticated(exist, null, exist.getAuthorities());
        VerifiedTokenResponce verifiedDTO = VerifiedTokenResponce.builder().email(user.getEmail()).build();
        TokenModel expected = new TokenModel("1", "accessToken", "refreshToken");

        when(googleAuthApiClient.getVerifiedToken(idToken)).thenReturn(verifiedDTO);
        when(userRepository.findByEmail(verifiedDTO.getEmail())).thenReturn(Optional.of(exist));
        when(tokenGenerator.createToken(authentication)).thenReturn(expected);
        assertEquals(expected, jwtService.oidc(OpenIdConnectionRequest.builder().idToken(idToken).build()));
    }

    @Test
    void oidcRegisterNew() {
        String idToken = "TestOidcToken";
        User user = User.builder().email("TestUser")
                .role(Role.USER).provider(Provider.GOOGLE).build();

        Authentication authentication = UsernamePasswordAuthenticationToken.
                authenticated(user, null, user.getAuthorities());
        VerifiedTokenResponce verifiedDTO = VerifiedTokenResponce.builder().email(user.getEmail()).build();
        TokenModel expected = new TokenModel("1", "accessToken", "refreshToken");

        when(googleAuthApiClient.getVerifiedToken(idToken)).thenReturn(verifiedDTO);
        when(userRepository.findByEmail(verifiedDTO.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);
        when(tokenGenerator.createToken(authentication)).thenReturn(expected);
        assertEquals(expected, jwtService.oidc(OpenIdConnectionRequest.builder().idToken(idToken).build()));
    }
}
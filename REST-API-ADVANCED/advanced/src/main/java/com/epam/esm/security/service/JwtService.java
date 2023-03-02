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
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtService {

    private final TokenGenerator tokenGenerator;
    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final GoogleAuthApiClient googleAuthApiClient;
    private final UserRepository userRepository;
    private final JwtAuthenticationProvider jwtRefresherTokenAuthProvider;
    private final PasswordEncoder passwordEncoder;

    public TokenModel register(AuthUserRequest signupUser) {
        User user = User.builder()
                .email(signupUser.getUsername())
                .password(passwordEncoder.encode(signupUser.getPassword()))
                .role(Role.USER)
                .provider(Provider.BASIC).build();
        if (!userRepository.existsByEmail(signupUser.getUsername())) {
            userRepository.save(user);
            Authentication authentication = UsernamePasswordAuthenticationToken.
                    authenticated(
                            user,
                            signupUser.getPassword(),
                            user.getAuthorities());
            return tokenGenerator.createToken(authentication);
        } else throw new UserInvalidRequestException("User with ( name =  " + signupUser.getUsername()
                + ") already exist. This field must be unique, change it and try again.");
    }

    public TokenModel login(AuthUserRequest loginUser) {
        Authentication authentication = daoAuthenticationProvider.
                authenticate(UsernamePasswordAuthenticationToken.
                        unauthenticated(loginUser.getUsername(), loginUser.getPassword()));
        return tokenGenerator.createToken(authentication);
    }

    public TokenModel refresh(TokenModel tokenModel) {
        if (StringUtils.isNotEmpty(tokenModel.getRefreshToken())) {
            Authentication authentication = jwtRefresherTokenAuthProvider.
                    authenticate(new BearerTokenAuthenticationToken(tokenModel.getRefreshToken()));
            return tokenGenerator.createToken(authentication);
        }
        throw new TokenBadRequestException();
    }

    public TokenModel oidc(OpenIdConnectionRequest oidc) {
        Authentication authentication;
        VerifiedTokenResponce verifiedToken = googleAuthApiClient.getVerifiedToken(oidc.getIdToken());
        Optional<User> optionalUser = userRepository.findByEmail(verifiedToken.getEmail());
        if (optionalUser.isPresent()) {
            User updateUser = optionalUser.get();
            if (updateUser.isGoogleProvider()) {
                authentication = UsernamePasswordAuthenticationToken.
                        authenticated(updateUser, null, updateUser
                                .getAuthorities());
            } else {
                updateUser.setProvider(Provider.GOOGLE);
                User save = userRepository.save(updateUser);
                authentication = UsernamePasswordAuthenticationToken.
                        authenticated(save, null, save.getAuthorities());

            }
        } else {
            User newUser = User.builder()
                    .email(verifiedToken.getEmail())
                    .role(Role.USER)
                    .provider(Provider.GOOGLE).build();
            userRepository.save(newUser);
            authentication = UsernamePasswordAuthenticationToken.
                    authenticated(newUser, null, newUser.getAuthorities());
        }
        return tokenGenerator.createToken(authentication);
    }
}

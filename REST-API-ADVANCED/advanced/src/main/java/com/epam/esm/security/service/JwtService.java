package com.epam.esm.security.service;

import com.epam.esm.security.TokenGenerator;
import com.epam.esm.security.dto.AuthUserDTO;
import com.epam.esm.security.dto.OidcDTO;
import com.epam.esm.security.dto.TokenDTO;
import com.epam.esm.security.exception.TokenBadRequestException;
import com.epam.esm.security.feign.client.FeignClientsUtil;
import com.epam.esm.security.feign.dto.TokenVerifiedDTO;
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
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtService {

    private final UserDetailsManager userDetailsManager;
    private final TokenGenerator tokenGenerator;
    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final FeignClientsUtil feignClientsUtil;
    private final UserRepository userRepository;
    private final JwtAuthenticationProvider jwtRefresherTokenAuthProvider;

    public TokenDTO register(AuthUserDTO signupUser) {
        User user = User.builder().email(signupUser.getUsername()).password(signupUser.getPassword())
                .role(Role.USER).provider(Provider.SELF).build();
        if (!userDetailsManager.userExists(signupUser.getUsername())) {
            userDetailsManager.createUser(user);
            Authentication authentication = UsernamePasswordAuthenticationToken.
                    authenticated(user, signupUser.getPassword(), user.getAuthorities());
            return tokenGenerator.createToken(authentication);
        } else throw new UserInvalidRequestException("User with ( name =  " + signupUser.getUsername()
                + ") already exist. This field must be unique, change it and try again.");
    }

    public TokenDTO login(AuthUserDTO loginUser) {
        Authentication authentication = daoAuthenticationProvider.
                authenticate(UsernamePasswordAuthenticationToken.
                        unauthenticated(loginUser.getUsername(), loginUser.getPassword()));
        return tokenGenerator.createToken(authentication);
    }

    public TokenDTO refresh(TokenDTO tokenDTO) {
        if (StringUtils.isNotEmpty(tokenDTO.getRefreshToken())) {
            Authentication authentication = jwtRefresherTokenAuthProvider.
                    authenticate(new BearerTokenAuthenticationToken(tokenDTO.getRefreshToken()));
            return tokenGenerator.createToken(authentication);
        }
        throw new TokenBadRequestException();
    }

    public TokenDTO oidc(OidcDTO oidcDTO) {
        Authentication authentication;
        TokenVerifiedDTO verifiedToken = feignClientsUtil.getVerifiedToken(oidcDTO.getIdToken());
        Optional<User> optionalUser = userRepository.findByEmail(verifiedToken.getEmail());
        if (optionalUser.isPresent()) {
            if (optionalUser.get().getProvider().equals(Provider.GOOGLE)) {
                authentication = UsernamePasswordAuthenticationToken.
                        authenticated(optionalUser.get(), null, optionalUser.get()
                                .getAuthorities());
            } else {
                User updateUser = optionalUser.get();
                updateUser.setProvider(Provider.GOOGLE);
                User save = userRepository.save(updateUser);
                authentication = UsernamePasswordAuthenticationToken.
                        authenticated(save, null, save.getAuthorities());

            }
        } else {
            User newUser = User.builder().email(verifiedToken.getEmail()).role(Role.USER)
                    .provider(Provider.GOOGLE).build();
            userDetailsManager.createUser(newUser);
            authentication = UsernamePasswordAuthenticationToken.
                    authenticated(newUser, null, newUser.getAuthorities());
        }
        return tokenGenerator.createToken(authentication);
    }
}

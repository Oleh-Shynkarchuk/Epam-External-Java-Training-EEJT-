package com.epam.esm.security.controller;

import com.epam.esm.security.TokenGenerator;
import com.epam.esm.security.dto.AuthUserDTO;
import com.epam.esm.security.dto.OidcDTO;
import com.epam.esm.security.dto.TokenDTO;
import com.epam.esm.security.feign.FeignClientsUtil;
import com.epam.esm.security.feign.dto.TokenVerifiedDTO;
import com.epam.esm.user.entity.User;
import com.epam.esm.user.entity.provider.Provider;
import com.epam.esm.user.entity.roles.Role;
import com.epam.esm.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/v1/api/auth")
public class AuthController {

    private final UserDetailsManager userDetailsManager;
    private final TokenGenerator tokenGenerator;
    private final DaoAuthenticationProvider daoAuthenticationProvider;
    @Qualifier("jwtRefresherTokenAuthProvider")
    private final JwtAuthenticationProvider refreshTokenProvider;
    private final FeignClientsUtil feignClientsUtil;
    private final UserRepository userRepository;

    @Autowired
    public AuthController(UserDetailsManager userDetailsManager,
                          TokenGenerator tokenGenerator,
                          DaoAuthenticationProvider daoAuthenticationProvider,
                          JwtAuthenticationProvider refreshTokenProvider,
                          FeignClientsUtil feignClientsUtil,
                          UserRepository userRepository) {
        this.userDetailsManager = userDetailsManager;
        this.tokenGenerator = tokenGenerator;
        this.daoAuthenticationProvider = daoAuthenticationProvider;
        this.refreshTokenProvider = refreshTokenProvider;
        this.feignClientsUtil = feignClientsUtil;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<TokenDTO> register(@RequestBody AuthUserDTO signupUser) {
        User user = User.builder().email(signupUser.getUsername()).password(signupUser.getPassword())
                .role(Role.USER).provider(Provider.SELF).build();
        if (!userDetailsManager.userExists(signupUser.getUsername())) {
            userDetailsManager.createUser(user);
            Authentication authentication = UsernamePasswordAuthenticationToken.
                    authenticated(user, signupUser.getPassword(), user.getAuthorities());
            return ResponseEntity.ok(tokenGenerator.createToken(authentication));
        }
        throw new RuntimeException("USER EXIST");
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody AuthUserDTO loginUser) {
        Authentication authentication = daoAuthenticationProvider.
                authenticate(UsernamePasswordAuthenticationToken.
                        unauthenticated(loginUser.getUsername(), loginUser.getPassword()));
        return ResponseEntity.ok(tokenGenerator.createToken(authentication));
    }

    @PostMapping("/token")
    public ResponseEntity<TokenDTO> token(@RequestBody TokenDTO tokenDTO) {
        Authentication authentication = refreshTokenProvider.
                authenticate(new BearerTokenAuthenticationToken(tokenDTO.getRefreshToken()));
        return ResponseEntity.ok(tokenGenerator.createToken(authentication));
    }

    @PostMapping("/oidc")
    public ResponseEntity<TokenDTO> google(@RequestBody OidcDTO oidcDTO) {
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
        return ResponseEntity.ok(tokenGenerator.createToken(authentication));
    }
}

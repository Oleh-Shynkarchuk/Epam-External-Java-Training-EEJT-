package com.epam.esm.security.controller;

import com.epam.esm.security.dto.AuthUserDTO;
import com.epam.esm.security.dto.OidcDTO;
import com.epam.esm.security.dto.TokenDTO;
import com.epam.esm.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/auth")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthController {

    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<TokenDTO> register(@RequestBody AuthUserDTO signupUser) {
        TokenDTO register = jwtService.register(signupUser);
        return ResponseEntity.ok(register);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody AuthUserDTO loginUser) {
        TokenDTO login = jwtService.login(loginUser);
        return ResponseEntity.ok(login);
    }

    @PostMapping("/token")
    public ResponseEntity<TokenDTO> token(@RequestBody TokenDTO tokenDTO) {
        TokenDTO refresh = jwtService.refresh(tokenDTO);
        return ResponseEntity.ok(refresh);
    }

    @PostMapping("/oidc")
    public ResponseEntity<TokenDTO> oidc(@RequestBody OidcDTO oidcDTO) {
        TokenDTO oidc = jwtService.oidc(oidcDTO);
        return ResponseEntity.ok(oidc);
    }
}

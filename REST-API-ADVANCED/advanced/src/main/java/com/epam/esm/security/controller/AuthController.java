package com.epam.esm.security.controller;

import com.epam.esm.security.model.AuthUserModel;
import com.epam.esm.security.model.OpenIdConnectionModel;
import com.epam.esm.security.model.TokenModel;
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
    public ResponseEntity<TokenModel> register(@RequestBody AuthUserModel signupUser) {
        TokenModel register = jwtService.register(signupUser);
        return ResponseEntity.ok(register);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenModel> login(@RequestBody AuthUserModel loginUser) {
        TokenModel login = jwtService.login(loginUser);
        return ResponseEntity.ok(login);
    }

    @PostMapping("/token")
    public ResponseEntity<TokenModel> token(@RequestBody TokenModel tokenModel) {
        TokenModel refresh = jwtService.refresh(tokenModel);
        return ResponseEntity.ok(refresh);
    }

    @PostMapping("/oidc")
    public ResponseEntity<TokenModel> oidc(@RequestBody OpenIdConnectionModel oidc) {
        TokenModel token = jwtService.oidc(oidc);
        return ResponseEntity.ok(token);
    }
}

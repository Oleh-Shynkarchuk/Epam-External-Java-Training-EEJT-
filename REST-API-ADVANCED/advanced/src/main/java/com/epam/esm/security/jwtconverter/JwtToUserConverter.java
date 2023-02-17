package com.epam.esm.security.jwtconverter;

import com.epam.esm.user.entity.User;
import com.epam.esm.user.sevice.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;


@Component
public class JwtToUserConverter implements Converter<Jwt, UsernamePasswordAuthenticationToken> {
    private final UserService userService;

    @Autowired
    public JwtToUserConverter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UsernamePasswordAuthenticationToken convert(@NotNull Jwt source) {
        System.out.println(source);
        User user = User.builder().id(Long.valueOf(source.getSubject())).build();
        user.setRole(userService.getUserById(user.getId()).getRole());
        return new UsernamePasswordAuthenticationToken(user, source, user.getAuthorities());
    }
}

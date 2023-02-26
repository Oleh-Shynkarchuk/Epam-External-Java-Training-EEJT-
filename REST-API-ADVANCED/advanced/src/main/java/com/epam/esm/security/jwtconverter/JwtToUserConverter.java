package com.epam.esm.security.jwtconverter;

import com.epam.esm.user.entity.User;
import com.epam.esm.user.entity.provider.Provider;
import com.epam.esm.user.entity.roles.Role;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;


@Component
public class JwtToUserConverter implements Converter<Jwt, UsernamePasswordAuthenticationToken> {

    @Override
    public UsernamePasswordAuthenticationToken convert(@NotNull Jwt source) {
        User user = User.builder().id(Long.valueOf(source.getSubject()))
                .role(Role.valueOf(source.getClaim("role")))
                .provider(Provider.valueOf(source.getClaim("provider"))).build();
        return new UsernamePasswordAuthenticationToken(user, source, user.getAuthorities());
    }
}

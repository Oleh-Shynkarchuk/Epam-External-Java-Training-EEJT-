//package com.epam.esm.security.filter;
//
//import com.epam.esm.user.entity.User;
//import com.epam.esm.user.exception.UserInvalidRequestException;
//import org.apache.commons.lang3.ObjectUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.jetbrains.annotations.NotNull;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.time.Duration;
//import java.time.Instant;
//import java.util.*;
//
//@Component
//public class CustomFilter extends OncePerRequestFilter {
//    private final JwtDecoder jwtDecoder;
//
//    public CustomFilter(JwtDecoder jwtDecoder) {
//        this.jwtDecoder = jwtDecoder;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
//        final String bearerToken = request.getHeader("Authorization");
//        if (StringUtils.isEmpty(bearerToken) || !bearerToken.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//        if (!validateToken(jwtDecoder.decode(bearerToken))) {
//            filterChain.doFilter(request, response);
//        }else {
//            throw new UserInvalidRequestException("Invalid Token");
//        }
//    }
//
//    private boolean validateToken(Jwt decode) {
//        System.out.println(isTokenExpired(decode.getExpiresAt()));
//        System.out.println(decode.getIssuer());
//        System.out.println(decode.getId());
//        decode.getClaims().forEach((s, o) -> System.out.println(s+"!"+o));
//        decode.getHeaders().forEach((s, o) -> System.out.println(s+"||"+o));
//        System.out.println(decode.getAudience());
//        return isTokenExpired(decode.getExpiresAt());
//    }
//
////    private boolean validateToken(HashMap<String, Object> token, User user) {
////        return !isTokenExpired(Long.parseLong(token.get("exp").toString())) && !ObjectUtils.isEmpty(user);
////    }
//
//    private boolean isTokenExpired(Instant expiredAt) {
//        return Duration.between(Instant.now(), expiredAt).isNegative();
//    }
//}

package com.epam.esm.security;

import com.epam.esm.security.exception.SecurityAccessDeniedException;
import com.epam.esm.user.entity.User;
import com.epam.esm.user.entity.roles.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityAccessServiceTest {
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;
    @InjectMocks
    private SecurityAccessService securityAccessService;

    @Test
    void hasAccess() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication()).
                thenReturn(new UsernamePasswordAuthenticationToken(
                        User.builder().id(1L).role(Role.ADMIN).build(),
                        null));
        String request = "2";
        assertTrue(securityAccessService.hasAccess(request));
    }

    @Test
    void hasAccessShouldThrowAccessDeniedWhenUserRoleCheckOtherAccount() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication()).
                thenReturn(new UsernamePasswordAuthenticationToken(
                        User.builder().id(1L).role(Role.USER).build(),
                        null));
        String request = "2";
        assertThrows(SecurityAccessDeniedException.class, () -> securityAccessService.hasAccess(request));
    }
}
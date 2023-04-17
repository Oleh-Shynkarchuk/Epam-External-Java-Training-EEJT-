package com.epam.esm.security;

import com.epam.esm.security.exception.SecurityAccessDeniedException;
import com.epam.esm.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("securityService")
@Slf4j
public class SecurityAccessService {
    public boolean hasAccess(String id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Requested id = {}, token id {}, token role {}", id, user.getId(), user.getRole());
        if (user.getId().equals(id) || user.isAdmin()) {
            return true;
        }
        throw new SecurityAccessDeniedException("Access denied.");
    }
}
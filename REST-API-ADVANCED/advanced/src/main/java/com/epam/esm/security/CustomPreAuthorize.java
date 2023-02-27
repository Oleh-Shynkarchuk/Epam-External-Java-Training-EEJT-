package com.epam.esm.security;

import com.epam.esm.security.exception.SecurityAccessDeniedException;
import com.epam.esm.user.entity.User;
import com.epam.esm.user.entity.roles.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("securityService")
@Slf4j
public class CustomPreAuthorize {
    public boolean customFilter(String id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Requested id = {}, token id {}, token role {}", id, user.getId(), user.getRole());
        if (!(user.getId().equals(Long.parseLong(id)) || user.getRole().equals(Role.ADMIN))) {
            throw new SecurityAccessDeniedException("Access denied.");
        }
        return true;
    }
}
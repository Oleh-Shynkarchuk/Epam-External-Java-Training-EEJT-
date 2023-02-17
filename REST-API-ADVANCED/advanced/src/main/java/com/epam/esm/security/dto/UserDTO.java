package com.epam.esm.security.dto;

import com.epam.esm.user.entity.User;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDTO {
    private String id;
    private String username;

    public static UserDTO from(User user) {
        return builder().id(String.valueOf(user.getId())).username(user.getUsername()).build();
    }

    ;
}

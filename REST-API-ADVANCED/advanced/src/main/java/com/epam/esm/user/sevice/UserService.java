package com.epam.esm.user.sevice;

import com.epam.esm.user.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    User getUserById(long id);

    List<User> getAllUsers(Pageable pageable);
}

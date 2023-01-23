package com.epam.esm.user.sevice;

import com.epam.esm.user.entity.User;

import java.util.List;

public interface UserService {

    User getUserById(long id);

    List<User> getAllUser(String page, String size);

//    List<Order> getUserOrders(Long id);

    User createUser(User user);
}

package com.epam.esm.user.controller;

import com.epam.esm.Validate;
import com.epam.esm.certificate.controller.PaginationCriteria;
import com.epam.esm.user.entity.User;
import com.epam.esm.user.sevice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUser(PaginationCriteria paginationCriteria) {
        Validate.pagination(paginationCriteria);
        return userService.getAllUser(paginationCriteria.getPage(), paginationCriteria.getSize());
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    //    @GetMapping("/{id}/orders")
//    public List<Order> getUserOrders(@PathVariable Long id){
//        return userService.getUserOrders(id);
//    }
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }
}

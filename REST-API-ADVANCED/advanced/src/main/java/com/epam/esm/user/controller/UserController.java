package com.epam.esm.user.controller;

import com.epam.esm.errorhandle.Validate;
import com.epam.esm.hateoas.HateoasSupport;
import com.epam.esm.user.entity.User;
import com.epam.esm.user.exception.UserInvalidRequestException;
import com.epam.esm.user.sevice.UserService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/v1/api/user")
public class UserController {

    private final UserService userService;
    private final HateoasSupport hateoasSupport;
    private final Validate validate;

    @Autowired
    public UserController(UserService userService, HateoasSupport hateoasSupport, Validate validate) {
        this.userService = userService;
        this.hateoasSupport = hateoasSupport;
        this.validate = validate;

    }

    @GetMapping
    public ResponseEntity<CollectionModel<User>> getAllUser(@ParameterObject Pageable paginationCriteria) {
        return ResponseEntity.ok(
                hateoasSupport.addHateoasSupportToUserList(
                        userService.getAllUser(paginationCriteria), paginationCriteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id) {
        if (validate.id(id)) {
            return ResponseEntity.ok(
                    hateoasSupport.addHateoasSupportToSingleUser(
                            userService.getUserById(Long.parseLong(id))));
        } else throw new UserInvalidRequestException("Invalid input ( id = " + id
                + " ). Valid only positive number ( 1 and more ).");
    }
}

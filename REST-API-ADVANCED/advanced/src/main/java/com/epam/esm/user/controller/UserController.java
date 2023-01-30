package com.epam.esm.user.controller;

import com.epam.esm.errorhandle.validation.Validate;
import com.epam.esm.hateoas.HateoasSupport;
import com.epam.esm.user.entity.User;
import com.epam.esm.user.exception.UserInvalidRequestException;
import com.epam.esm.user.sevice.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
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
        log.debug("Request accepted getAllUser. Send request to service");
        List<User> allUser = userService.getAllUser(paginationCriteria);

        log.debug("Add hateoas to user");
        CollectionModel<User> usersAndHateoas = hateoasSupport.addHateoasSupportToUserList(allUser, paginationCriteria);

        log.debug("Send response to client");
        return ResponseEntity.ok(usersAndHateoas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id) {

        log.debug("Request accepted getUserById. Validate id field.");
        if (validate.isPositiveAndParsableId(id)) {

            log.debug("Send request to service");
            User userById = userService.getUserById(Long.parseLong(id));

            log.debug("Add hateoas to user");
            User userAndHateoas = hateoasSupport.addHateoasSupportToSingleUser(userById);

            log.debug("Send response to client");
            return ResponseEntity.ok(userAndHateoas);
        } else {
            log.error("Invalid input ( id = " + id
                    + " ). Only a positive number is allowed ( 1 and more ).");
            throw new UserInvalidRequestException("Invalid input ( id = " + id
                    + " ). Only a positive number is allowed ( 1 and more ).");
        }
    }
}

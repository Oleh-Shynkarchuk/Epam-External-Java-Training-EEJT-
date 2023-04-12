package com.epam.esm.user.controller;

import com.epam.esm.user.entity.User;
import com.epam.esm.user.hateoas.UserHateoasSupport;
import com.epam.esm.user.sevice.EcommerceUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v2/api/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EcommerceUserController {

    private final EcommerceUserService userService;
    private final UserHateoasSupport hateoasSupport;


    @GetMapping
    public ResponseEntity<CollectionModel<User>> getAllUser(@ParameterObject Pageable paginationCriteria) {
        log.debug("Request accepted getAllUsers. " +
                "Pagination object request = " + paginationCriteria.toString());
        List<User> allUser = userService.getAllUsers(paginationCriteria);
        CollectionModel<User> usersAndHateoas = hateoasSupport.addHateoasSupportToUserList(allUser, paginationCriteria);
        log.debug("Send response users list: " + usersAndHateoas.toString() + " to client");
        return ResponseEntity.ok(usersAndHateoas);
    }

    @GetMapping("/{id}")
//    @PreAuthorize("@securityService.hasAccess(#id)")
    public ResponseEntity<User> getUser(@PathVariable String id) {
        log.debug("Request accepted getUserById. " +
                "Id param request = " + id);
        User userById = userService.getUserById(id);
        User userAndHateoas = hateoasSupport.addHateoasSupportToSingleUser(userById);
        log.debug("Send response user : " + userAndHateoas.toString() + " to client");
        return ResponseEntity.ok(userAndHateoas);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User newUser) {
        log.debug("Request accepted createUser. " +
                "createUser param request = " + newUser);
        User userById = userService.createUser(newUser);
        User userAndHateoas = hateoasSupport.addHateoasSupportToSingleUser(userById);
        log.debug("Return new user : " + userAndHateoas.toString() + " to client");
        return ResponseEntity.ok(userAndHateoas);
    }
}

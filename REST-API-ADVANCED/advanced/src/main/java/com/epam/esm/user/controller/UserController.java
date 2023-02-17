package com.epam.esm.user.controller;

import com.epam.esm.Validator;
import com.epam.esm.user.entity.User;
import com.epam.esm.user.exception.UserInvalidRequestException;
import com.epam.esm.user.hateoas.UserHateoasSupport;
import com.epam.esm.user.sevice.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final UserHateoasSupport hateoasSupport;
    private final Validator validator;

    @Autowired
    public UserController(UserService userService, UserHateoasSupport hateoasSupport, Validator validator) {
        this.userService = userService;
        this.hateoasSupport = hateoasSupport;
        this.validator = validator;

    }

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
    @PreAuthorize("#id == #user.id.toString()")
    public ResponseEntity<User> getUser(@AuthenticationPrincipal User user, @PathVariable String id) {
        log.debug("Request accepted getUserById. " +
                "Id param request = " + id);
        String idResponse = validator.isPositiveAndParsableIdResponse(id);
        if (idResponse.isEmpty()) {
            User userById = userService.getUserById(Long.parseLong(id));
            User userAndHateoas = hateoasSupport.addHateoasSupportToSingleUser(userById);
            log.debug("Send response user : " + userAndHateoas.toString() + " to client");
//            return ResponseEntity.ok(UserDTO.from(userAndHateoas));
            return ResponseEntity.ok(userAndHateoas);
        }
        log.error(idResponse);
        throw new UserInvalidRequestException(idResponse);
    }
}

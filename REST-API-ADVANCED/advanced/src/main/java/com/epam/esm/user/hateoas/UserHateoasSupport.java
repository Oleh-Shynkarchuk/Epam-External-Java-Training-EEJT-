package com.epam.esm.user.hateoas;

import com.epam.esm.user.controller.UserController;
import com.epam.esm.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserHateoasSupport {
    public CollectionModel<User> addHateoasSupportToUserList(List<User> allUser, Pageable paginationCriteria) {
        allUser.forEach(this::addHateoasSupport);
        return CollectionModel.of(allUser).add(
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                        .getAllUser(paginationCriteria)).withRel("getAllUsers")
        );
    }

    public User addHateoasSupportToSingleUser(User user) {
        return user.add(
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                        .getUser(user, String.valueOf(user.getId()))).withRel("getUser"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                        .getAllUser(Pageable.unpaged())).withRel("getAllUsers"));
    }

    public void addHateoasSupport(User user) {
        if (user.getLinks().isEmpty()) {
            user.add(
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                            .getUser(user, String.valueOf(user.getId()))).withRel("getUser"));
        }
    }
}

package com.epam.esm.user.sevice;

import com.epam.esm.errorhandle.constants.ErrorConstants;
import com.epam.esm.errorhandle.validation.Validate;
import com.epam.esm.user.entity.User;
import com.epam.esm.user.exception.UserNotFoundException;
import com.epam.esm.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Validate validate;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, Validate validate) {
        this.userRepository = userRepository;
        this.validate = validate;
    }

    @Override
    public List<User> getAllUser(Pageable paginationCriteria) {
        log.debug("Start of get all users method in service layer." +
                "For valid non erroneous pageable request get amount of all users in repository");
        long amountOfAllUsers = userRepository.count();

        log.debug("Validate pagination request.");
        Pageable pageable = validate.validNonErroneousPageableRequest(amountOfAllUsers, paginationCriteria);

        log.debug("Get users from repository with pagination");
        Page<User> allUsers = userRepository.findAll(pageable);

        log.debug("Emptiness check.");
        if (allUsers.isEmpty()) {
            throw getUserNotFoundException();
        }
        log.debug("Service send received certificates from repository to controller");
        return allUsers.toList();
    }

    @Override
    public User getUserById(long id) {
        log.debug("Start of getUserById method in service layer. " +
                "Get user by id from repository");
        User user = userRepository.findById(id).orElseThrow(this::getUserNotFoundException);

        log.debug("Service send received user from repository");
        return user;
    }

    private UserNotFoundException getUserNotFoundException() {
        log.error(ErrorConstants.USER_NOT_FOUND_MESSAGE);
        return new UserNotFoundException(ErrorConstants.USER_NOT_FOUND_MESSAGE);
    }
}

package com.epam.esm.user.sevice;

import com.epam.esm.errorhandle.constants.ErrorConstants;
import com.epam.esm.errorhandle.validation.Validate;
import com.epam.esm.user.entity.User;
import com.epam.esm.user.exception.UserNotFoundException;
import com.epam.esm.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public User getUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(ErrorConstants.USER_NOT_FOUND_MESSAGE));
    }

    @Override
    public List<User> getAllUser(Pageable pageable) {
        return userRepository.findAll(
                validate.validNonErroneousPageableRequest(
                        userRepository.count(),
                        pageable
                )
        ).toList();
    }
}

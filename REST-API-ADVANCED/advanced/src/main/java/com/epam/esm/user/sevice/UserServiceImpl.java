package com.epam.esm.user.sevice;

import com.epam.esm.ErrorConstants;
import com.epam.esm.Validator;
import com.epam.esm.user.entity.User;
import com.epam.esm.user.exception.UserNotFoundException;
import com.epam.esm.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Primary
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final Validator validator;

    @Override
    public List<User> getAllUsers(Pageable paginationCriteria) {
        log.debug("Start of get all users method in service layer." +
                "For valid non erroneous pageable request get amount of all users in repository");
        long amountOfAllUsers = userRepository.count();
        Pageable pageable = validator.validPageableRequest(amountOfAllUsers, paginationCriteria);
        Page<User> allUsers = userRepository.findAll(pageable);
        log.debug("Emptiness check.");
        if (allUsers.isEmpty()) {
            throw userNotFoundException();
        }
        return allUsers.toList();
    }

    @Override
    public User getUserById(long id) {
        log.debug("Start of getUserById method in service layer. " +
                "Get user by id from repository");
        return userRepository.findById(id).orElseThrow(this::userNotFoundException);
    }

    private UserNotFoundException userNotFoundException() {
        log.error(ErrorConstants.USER_NOT_FOUND_MESSAGE);
        return new UserNotFoundException();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        MessageFormat.format("username {0} not found", username)));
    }
}

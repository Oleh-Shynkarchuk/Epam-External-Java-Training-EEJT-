package com.epam.esm.user.sevice;

import com.epam.esm.Validator;
import com.epam.esm.user.entity.User;
import com.epam.esm.user.exception.UserNotFoundException;
import com.epam.esm.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private Validator validator;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getAllUser() {
        long availableAmountOfOrders = 2L;
        Pageable pageable = PageRequest.of(0, 20);
        User user1 = User.builder().id(1L).build();
        User user2 = User.builder().id(2L).build();
        Page<User> expected = new PageImpl<>(List.of(user1, user2), pageable, availableAmountOfOrders);

        Mockito.when(userRepository.count()).thenReturn(availableAmountOfOrders);
        Mockito.when(validator.validPageableRequest(availableAmountOfOrders, pageable)).thenReturn(pageable);
        Mockito.when(userRepository.findAll(pageable)).thenReturn(expected);

        assertEquals(expected.toList(), userService.getAllUsers(pageable));
    }

    @Test
    void getAllUserShouldThrowItemNotFound() {
        long availableAmountOfOrders = 0L;
        Pageable pageable = PageRequest.of(0, 20);

        Page<User> expected = new PageImpl<>(List.of(), pageable, availableAmountOfOrders);

        Mockito.when(userRepository.count()).thenReturn(availableAmountOfOrders);
        Mockito.when(validator.validPageableRequest(availableAmountOfOrders, pageable)).thenReturn(pageable);
        Mockito.when(userRepository.findAll(pageable)).thenReturn(expected);

        assertThrows(UserNotFoundException.class, () -> userService.getAllUsers(pageable));
    }

    @Test
    void getUserById() {
        long requestId = 1L;
        User expected = User.builder().id(1L).build();

        Mockito.when(userRepository.findById(requestId)).thenReturn(Optional.of(expected));

        assertEquals(expected, userService.getUserById(requestId));
    }

    @Test
    void getUserByIdShouldThrowItemNotFound() {
        long requestId = 1L;

        Mockito.when(userRepository.findById(requestId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(requestId));
    }
}
package com.epam.esm.user.controller;

import com.epam.esm.ErrorConstants;
import com.epam.esm.ErrorController;
import com.epam.esm.Validator;
import com.epam.esm.user.entity.User;
import com.epam.esm.user.entity.roles.Role;
import com.epam.esm.user.hateoas.UserHateoasSupport;
import com.epam.esm.user.sevice.UserServiceImpl;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserHateoasSupport hateoasSupport;
    @Mock
    private Validator validator;
    @Mock
    private UserServiceImpl userService;
    @InjectMocks
    private UserController userController;

    private final JsonMapper jsonMapper = new JsonMapper();

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new ErrorController())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void getAllUser() throws Exception {
        Pageable pageable = PageRequest.of(0, 20);
        User user1 = User.builder().id(1L).email("testUser1@mail.com").role(Role.USER).build();
        User user2 = User.builder().id(2L).email("testUser2@mail.com").role(Role.USER).build();
        List<User> userList = List.of(user1, user2);
        String expected = jsonMapper.writeValueAsString(CollectionModel.of(userList));

        Mockito.when(userService.getAllUsers(pageable))
                .thenReturn(userList);
        Mockito.when(hateoasSupport.addHateoasSupportToUserList(userList, pageable))
                .thenReturn(CollectionModel.of(userList));

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/v1/api/user"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        assertEquals(response, expected);
        Mockito.verify(userService).getAllUsers(pageable);
        Mockito.verify(hateoasSupport).addHateoasSupportToUserList(userList, pageable);
    }

    @Test
    void getUser() throws Exception {
        long request = 1L;
        User user1 = User.builder().id(1L).email("testUser1@mail.com").role(Role.USER).build();
        String expected = jsonMapper.writeValueAsString(user1);

        Mockito.when(validator.isPositiveAndParsableIdResponse(String.valueOf(request))).thenReturn("");
        Mockito.when(userService.getUserById(request))
                .thenReturn(user1);
        Mockito.when(hateoasSupport.addHateoasSupportToSingleUser(user1))
                .thenReturn(user1);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/v1/api/user/" + request))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        assertEquals(response, expected);
    }

    @Test
    void getUserShouldReturnInvalidRequest() throws Exception {
        long request = -1L;
        String expected = jsonMapper.writeValueAsString(Map.of("errorCode", ErrorConstants.USER_INVALID_REQUEST_ERROR_CODE,
                "errorMessage", "Invalid input ( id = " + request
                        + " ). Only a positive number is allowed ( 1 and more )."));

        Mockito.when(validator.isPositiveAndParsableIdResponse(String.valueOf(request)))
                .thenReturn("Invalid input ( id = " + request
                        + " ). Only a positive number is allowed ( 1 and more ).");

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/v1/api/user/" + request))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        assertEquals(response, expected);
    }
}
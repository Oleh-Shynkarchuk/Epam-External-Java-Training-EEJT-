package com.epam.esm.security.controller;

import com.epam.esm.ErrorConstants;
import com.epam.esm.security.dto.AuthUserDTO;
import com.epam.esm.security.dto.TokenDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest {

    public static final String MESSAGE = "errorMessage";
    public static final String CODE = "errorCode";

    private final TestRestTemplate restTemplate;

    @Autowired
    AuthControllerTest(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Test
    @SqlGroup({
            @Sql(scripts = {"classpath:create.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts =
                    "classpath:cleanup.sql")
    })
    void registerShouldReturnTokenDTO() {
        AuthUserDTO authUserDTO = new AuthUserDTO("NewTestUser", "TestPassword");
        HttpEntity<Object> requestEntity = new HttpEntity<>(authUserDTO);
        final String authorsUrl = restTemplate.getRootUri() + "/v1/api/auth/register";
        ResponseEntity<TokenDTO> responseEntity =
                restTemplate.exchange(authorsUrl, HttpMethod.POST, requestEntity, TokenDTO.class);
        assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType()),
                () -> {
                    if (responseEntity.getBody() != null) {
                        assertEquals(TokenDTO.class, responseEntity.getBody().getClass());
                    } else throw new Exception("TokenDTO was not created");
                }
        );
    }

    @Test
    @SqlGroup({
            @Sql(scripts = {"classpath:create.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts =
                    "classpath:cleanup.sql")
    })
    void registerShouldReturnExceptionCuzUserIsAlreadyExist() throws JsonProcessingException {
        AuthUserDTO authUserDTO = new AuthUserDTO("testUser1@mail.com",
                "TestPassword");
        HttpEntity<Object> requestEntity = new HttpEntity<>(authUserDTO);
        final String authorsUrl = restTemplate.getRootUri() + "/v1/api/auth/register";

        String expected = JsonMapper.builder().build().writeValueAsString
                (Map.of(CODE, ErrorConstants.USER_INVALID_REQUEST_ERROR_CODE,
                        MESSAGE, "User with ( name =  " + authUserDTO.getUsername()
                                + ") already exist. This field must be unique, change it and try again."));

        ResponseEntity<String> responseEntity =
                restTemplate.exchange(authorsUrl, HttpMethod.POST, requestEntity, String.class);

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode()),
                () -> assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType()),
                () -> assertEquals(expected, responseEntity.getBody())
        );
    }

    @Test
    @SqlGroup({
            @Sql(scripts = {"classpath:create.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts =
                    "classpath:cleanup.sql")
    })
    void login() {
        AuthUserDTO authUserDTO = new AuthUserDTO("testUser3@mail.com",
                "TestPassword");
        HttpEntity<Object> requestEntity = new HttpEntity<>(authUserDTO);

        final String loginAuthorsUrl = restTemplate.getRootUri() + "/v1/api/auth/register";
        restTemplate.exchange(loginAuthorsUrl, HttpMethod.POST, requestEntity, TokenDTO.class);

        final String authorsUrl = restTemplate.getRootUri() + "/v1/api/auth/login";

        ResponseEntity<TokenDTO> responseEntity =
                restTemplate.exchange(authorsUrl, HttpMethod.POST, requestEntity, TokenDTO.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType()),
                () -> {
                    if (responseEntity.getBody() != null) {
                        assertEquals(TokenDTO.class, responseEntity.getBody().getClass());
                    } else throw new Exception("TokenDTO was not created");
                }
        );
    }

    @Test
    @SqlGroup({
            @Sql(scripts = {"classpath:create.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts =
                    "classpath:cleanup.sql")
    })
    void loginShouldReturnBadCredentials() throws JsonProcessingException {
        AuthUserDTO authUserDTO = new AuthUserDTO("testUser1@mail.com",
                "WrongPassword");
        HttpEntity<Object> requestEntity = new HttpEntity<>(authUserDTO);
        final String authorsUrl = restTemplate.getRootUri() + "/v1/api/auth/login";

        String expected = "{\"" + CODE + "\":" + "\"" + HttpServletResponse.SC_UNAUTHORIZED
                + "\",\"" + MESSAGE + "\":" + "\"Bad credentials\"}";

        ResponseEntity<String> responseEntity =
                restTemplate.exchange(authorsUrl, HttpMethod.POST, requestEntity, String.class);

        assertAll(
                () -> assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode()),
                () -> assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType()),
                () -> {
                    if (responseEntity.getBody() != null) {
                        assertEquals(expected, responseEntity.getBody());
                    }
                }
        );
    }

    @Test
    @SqlGroup({
            @Sql(scripts = {"classpath:create.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts =
                    "classpath:cleanup.sql")
    })
    void token() throws Exception {

        AuthUserDTO authUserDTO = new AuthUserDTO("testUser3@mail.com",
                "TestPassword");
        HttpEntity<Object> requestEntity = new HttpEntity<>(authUserDTO);

        final String loginAuthorsUrl = restTemplate.getRootUri() + "/v1/api/auth/register";
        ResponseEntity<TokenDTO> responseEntity =
                restTemplate.exchange(loginAuthorsUrl, HttpMethod.POST, requestEntity, TokenDTO.class);

        final String tokenAuthorsUrl = restTemplate.getRootUri() + "/v1/api/auth/token";
        HttpEntity<Object> tokenRequestEntity;
        if (responseEntity.getBody() != null) {
            tokenRequestEntity = new HttpEntity<>(responseEntity.getBody());
        } else throw new Exception("RefreshToken does not found");

        ResponseEntity<TokenDTO> tokenDTOResponseEntity =
                restTemplate.exchange(tokenAuthorsUrl, HttpMethod.POST, tokenRequestEntity, TokenDTO.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK, tokenDTOResponseEntity.getStatusCode()),
                () -> assertEquals(MediaType.APPLICATION_JSON, tokenDTOResponseEntity.getHeaders().getContentType()),
                () -> {
                    if (tokenDTOResponseEntity.getBody() != null) {
                        assertEquals(TokenDTO.class, tokenDTOResponseEntity.getBody().getClass());
                    } else throw new Exception("TokenDTO was not created");
                }
        );
    }

    @Test
    @SqlGroup({
            @Sql(scripts = {"classpath:create.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts =
                    "classpath:cleanup.sql")
    })
    void RefreshTokenReturnBadRequestMessageCuzEmpty() throws JsonProcessingException {
        HttpEntity<Object> tokenRequestEntity = new HttpEntity<>(new TokenDTO());
        final String tokenAuthorsUrl = restTemplate.getRootUri() + "/v1/api/auth/token";
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(tokenAuthorsUrl, HttpMethod.POST, tokenRequestEntity, String.class);

        String expected = JsonMapper.builder().build().writeValueAsString(
                Map.of(CODE, ErrorConstants.SECURITY_INVALID_TOKEN,
                        MESSAGE, ErrorConstants.EMPTY_REFRESH_TOKEN_MESSAGE)
        );

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode()),
                () -> assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType()),
                () -> assertEquals(expected, responseEntity.getBody())
        );
    }

    @Test
    @SqlGroup({
            @Sql(scripts = {"classpath:create.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts =
                    "classpath:cleanup.sql")
    })
    void oidc() {
    }
}
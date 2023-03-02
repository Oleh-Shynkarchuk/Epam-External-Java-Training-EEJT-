package com.epam.esm.security.controller;

import com.epam.esm.ErrorConstants;
import com.epam.esm.security.model.AuthUserRequest;
import com.epam.esm.security.model.OpenIdConnectionRequest;
import com.epam.esm.security.model.TokenModel;
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
        AuthUserRequest authUserRequest = new AuthUserRequest("NewTestUser", "TestPassword");
        HttpEntity<Object> requestEntity = new HttpEntity<>(authUserRequest);
        final String authorsUrl = restTemplate.getRootUri() + "/v1/api/auth/register";
        ResponseEntity<TokenModel> responseEntity =
                restTemplate.exchange(authorsUrl, HttpMethod.POST, requestEntity, TokenModel.class);
        assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType()),
                () -> {
                    if (responseEntity.getBody() != null) {
                        assertEquals(TokenModel.class, responseEntity.getBody().getClass());
                    } else throw new Exception("TokenModel was not created");
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
        AuthUserRequest authUserRequest = new AuthUserRequest("testUser1@mail.com",
                "TestPassword");
        HttpEntity<Object> requestEntity = new HttpEntity<>(authUserRequest);
        final String authorsUrl = restTemplate.getRootUri() + "/v1/api/auth/register";

        String expected = JsonMapper.builder().build().writeValueAsString
                (Map.of(CODE, ErrorConstants.USER_INVALID_REQUEST_ERROR_CODE,
                        MESSAGE, "User with ( name =  " + authUserRequest.getUsername()
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
        AuthUserRequest authUserRequest = new AuthUserRequest("testUser3@mail.com",
                "TestPassword");
        HttpEntity<Object> requestEntity = new HttpEntity<>(authUserRequest);

        final String loginAuthorsUrl = restTemplate.getRootUri() + "/v1/api/auth/register";
        restTemplate.exchange(loginAuthorsUrl, HttpMethod.POST, requestEntity, TokenModel.class);

        final String authorsUrl = restTemplate.getRootUri() + "/v1/api/auth/login";

        ResponseEntity<TokenModel> responseEntity =
                restTemplate.exchange(authorsUrl, HttpMethod.POST, requestEntity, TokenModel.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType()),
                () -> {
                    if (responseEntity.getBody() != null) {
                        assertEquals(TokenModel.class, responseEntity.getBody().getClass());
                    } else throw new Exception("TokenModel was not created");
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
        AuthUserRequest authUserRequest = new AuthUserRequest("testUser1@mail.com",
                "WrongPassword");
        HttpEntity<Object> requestEntity = new HttpEntity<>(authUserRequest);
        final String authorsUrl = restTemplate.getRootUri() + "/v1/api/auth/login";

        String expected = JsonMapper.builder().build().writeValueAsString(Map.of(
                CODE, HttpServletResponse.SC_UNAUTHORIZED,
                MESSAGE, "Bad credentials"));
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

        AuthUserRequest authUserRequest = new AuthUserRequest("testUser3@mail.com",
                "TestPassword");
        HttpEntity<Object> requestEntity = new HttpEntity<>(authUserRequest);

        final String loginAuthorsUrl = restTemplate.getRootUri() + "/v1/api/auth/login";
        ResponseEntity<TokenModel> responseEntity =
                restTemplate.exchange(loginAuthorsUrl, HttpMethod.POST, requestEntity, TokenModel.class);

        final String tokenAuthorsUrl = restTemplate.getRootUri() + "/v1/api/auth/token";
        HttpEntity<Object> tokenRequestEntity;
        if (responseEntity.getBody() != null) {
            tokenRequestEntity = new HttpEntity<>(responseEntity.getBody());
        } else throw new Exception("RefreshToken does not found");

        ResponseEntity<TokenModel> tokenDTOResponseEntity =
                restTemplate.exchange(tokenAuthorsUrl, HttpMethod.POST, tokenRequestEntity, TokenModel.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK, tokenDTOResponseEntity.getStatusCode()),
                () -> assertEquals(MediaType.APPLICATION_JSON, tokenDTOResponseEntity.getHeaders().getContentType()),
                () -> {
                    if (tokenDTOResponseEntity.getBody() != null) {
                        assertEquals(TokenModel.class, tokenDTOResponseEntity.getBody().getClass());
                    } else throw new Exception("TokenModel was not created");
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
        HttpEntity<Object> tokenRequestEntity = new HttpEntity<>(new TokenModel());
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
    void oidcReturnInvalidValueMessageAndBadRequestStatus() throws JsonProcessingException {
        OpenIdConnectionRequest openIdConnectionRequest = OpenIdConnectionRequest.builder().idToken("eyJhbGciOiJSUzI1NiIsImtpZCI6ImI0OWM1MDYyZ" +
                        "Dg5MGY1Y2U0NDllODkwYzg4ZThkZDk4YzRmZWUwYWIiLCJ0eXAiOiJKV1QifQ.eyJpc3MiO" +
                        "iJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiIxMzA3MDAyNzI3NzYtMzkxc" +
                        "TMxNmNmazUxMWlhOTJjMGswOWRqYjM4b2U3OGsuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb2" +
                        "0iLCJhdWQiOiIxMzA3MDAyNzI3NzYtMzkxcTMxNmNmazUxMWlhOTJjMGswOWRqYjM4b2U3OGsu" +
                        "YXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDcyMzg4ODE0MzA2NDEyMDg0NDI" +
                        "iLCJlbWFpbCI6Im9sZWguc2h5bmthcmNodWtAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnR" +
                        "ydWUsImF0X2hhc2giOiIyc3VaaEpDYW1NbVJEVHVGSDhsd2pBIiwibmFtZSI6Ik9sZWggU2h5bmth" +
                        "cmNodWsiLCJwaWN0dXJlIjoiaHR0cHM6Ly9saDMuZ29vZ2xldXNlcmNvbnRlbnQuY29tL2EvQUVkR" +
                        "lRwNllFWGRzSHlISktOczdEQUJnZ2NmUEctOHQweUN4R0JyVkVianh5Zz1zOTYtYyIsImdpdmVuX25" +
                        "hbWUiOiJPbGVoIiwiZmFtaWx5X25hbWUiOiJTaHlua2FyY2h1ayIsImxvY2FsZSI6InVrIiwiaWF0I" +
                        "joxNjc2NDY5NDQ2LCJleHAiOjE2NzY0NzMwNDZ9.juW_na3PD7DkK93Cjdb0ElgJGa9kabjQp8GNJw" +
                        "I5f7eQaVDU_zTTLOnoQvmhqNiw1pA4uehTtVsREEhEqZUyhDzVpivgd3hFnE9evew21AN_1SGDO9R" +
                        "XKIpUDn16lPniDAp4kTGLNbTD5AGjGg-rWq2Es2ppYiYvabeN2rxI4u6ORYVSQqj1qXWC7wIim4rJ" +
                        "4efGxxMftam4huuu5XJZBaiDYKmMAYIEaU1ldgVnOyj7qJINHpRtHAA2eYyc34BvAbSseEpuKSHr5" +
                        "yZMO915woj96Jf1lM82oSyiIQ3ZTjzjcrTfkpPqG8OAAA3lPpmxGAvyKJhicBOc1ItmTG491Q")
                .build();
        HttpEntity<OpenIdConnectionRequest> tokenRequestEntity = new HttpEntity<>(openIdConnectionRequest);
        final String tokenAuthorsUrl = restTemplate.getRootUri() + "/v1/api/auth/oidc";
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(tokenAuthorsUrl, HttpMethod.POST, tokenRequestEntity, String.class);

        String expected = JsonMapper.builder().build().writeValueAsString(
                Map.of(CODE, ErrorConstants.SECURITY_INVALID_TOKEN,
                        MESSAGE, "Invalid Value")
        );

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode()),
                () -> assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType()),
                () -> assertEquals(expected, responseEntity.getBody())
        );
    }
}
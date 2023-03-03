package com.epam.esm.order.controller;

import com.epam.esm.ErrorConstants;
import com.epam.esm.certificate.entity.Certificate;
import com.epam.esm.order.entity.Order;
import com.epam.esm.security.model.AuthUserRequest;
import com.epam.esm.security.model.TokenModel;
import com.epam.esm.tag.entity.Tag;
import com.epam.esm.user.entity.User;
import com.epam.esm.user.entity.provider.Provider;
import com.epam.esm.user.entity.roles.Role;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerTest {

    private final TestRestTemplate restTemplate;
    private HttpEntity<Object> requestEntity;
    private HttpHeaders headers;
    @Autowired
    OrderControllerTest(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @BeforeEach
    void setUp() {

        AuthUserRequest authUserRequest = new AuthUserRequest("testUser3@mail.com",
                "TestPassword");
        HttpEntity<Object> loginEntity = new HttpEntity<>(authUserRequest);

        final String authorsUrl = restTemplate.getRootUri() + "/v1/api/auth/login";

        ResponseEntity<TokenModel> responseEntity =
                restTemplate.exchange(authorsUrl, HttpMethod.POST, loginEntity, TokenModel.class);
        headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        if (ObjectUtils.isNotEmpty(responseEntity.getBody())) {
            headers.setBearerAuth(responseEntity.getBody().getAccessToken());
        }
    }

    @Test
    @SqlGroup({
            @Sql(scripts = {"classpath:create.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts =
                    "classpath:cleanup.sql")
    })
    void getAllOrder() {
        URI uri = UriComponentsBuilder.fromUriString("/v1/api/order")
                .queryParam("page", "0")
                .queryParam("size", "5").build().encode().toUri();
        Tag tag1 = Tag.builder().id(1L).name("testTag1").build();
        Tag tag2 = Tag.builder().id(2L).name("testTag2").build();
        Tag tag3 = Tag.builder().id(3L).name("testTag3").build();
        Certificate certificate1 = Certificate.builder().id(1L).name("testCertificate1")
                .description("first description").
                durationOfDays("10").price(BigDecimal.valueOf(21050, 2))
                .createDate(LocalDateTime.parse("2022-12-24T12:51:55"))
                .lastUpdateDate(LocalDateTime.parse("2022-12-24T12:51:55")).tags(List.of(tag1, tag2)).build();
        Certificate certificate2 = Certificate.builder().id(2L).name("testCertificate2").durationOfDays("5").
                description("second description").price(BigDecimal.valueOf(14530, 2))
                .createDate(LocalDateTime.parse("2022-12-24T23:51:55"))
                .lastUpdateDate(LocalDateTime.parse("2022-12-24T23:51:55"))
                .tags(List.of(tag2, tag3)).build();
        List<Order> expected =
                List.of(
                        Order.builder().id(1L).totalPrice(BigDecimal.valueOf(35580, 2))
                                .purchaseDate(LocalDateTime.parse("2022-12-24T15:51:55")).user(
                                        User.builder().id(1L).email("testUser3@mail.com")
                                                .password("$2a$10$4vsKr.gKZtJV3TyCpOrePONcbkR7/RywupRuiPFVvZukCgfRNmSP.").role(Role.USER).build()).
                                certificates(List.of(certificate1, certificate2)).build(),

                        Order.builder().id(2L).totalPrice(BigDecimal.valueOf(14530, 2))
                                .purchaseDate(LocalDateTime.parse("2022-12-24T17:54:35")).user(
                                        User.builder().id(2L).email("testUser2@mail.com")
                                                .password("TestPassword").role(Role.ADMIN).build()).
                                certificates(List.of(certificate2)).build());
        final String authorsUrl = restTemplate.getRootUri() + "/v1/api/order";
        requestEntity = new HttpEntity<>(headers);
        ResponseEntity<CollectionModel<Order>> responseEntity =
                restTemplate.exchange(uri, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {
                });

        assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType()),
                () -> {
                    if (responseEntity.getBody() != null) {
                        assertEquals(expected, responseEntity.getBody().getContent().stream().toList());
                    }
                },
                () -> {
                    if (responseEntity.getBody() != null) {
                        assertTrue(responseEntity.getBody().getLinks().stream()
                                .allMatch(link -> link.getHref().contains(authorsUrl)));
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
    void getOrderById() {
        long id = 2L;
        URI uri = UriComponentsBuilder.fromUriString("/v1/api/order").build().encode().toUri();
        Tag tag2 = Tag.builder().id(2L).name("testTag2").build();
        Tag tag3 = Tag.builder().id(3L).name("testTag3").build();
        Certificate certificate2 = Certificate.builder().id(2L).name("testCertificate2").durationOfDays("5").
                description("second description").price(BigDecimal.valueOf(14530, 2))
                .createDate(LocalDateTime.parse("2022-12-24T23:51:55"))
                .lastUpdateDate(LocalDateTime.parse("2022-12-24T23:51:55"))
                .tags(List.of(tag2, tag3)).build();
        Order expected = Order.builder().id(id).totalPrice(BigDecimal.valueOf(14530, 2))
                .purchaseDate(LocalDateTime.parse("2022-12-24T17:54:35")).user(
                        User.builder().id(2L).email("testUser2@mail.com")
                                .password("TestPassword").role(Role.ADMIN).provider(Provider.BASIC).build()).
                certificates(List.of(certificate2)).build();
        final String authorsUrl = restTemplate.getRootUri() + "/v1/api/order";
        requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Order> responseEntity =
                restTemplate.exchange(uri + "/{id}", HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {
                }, id);

        assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType()),
                () -> assertEquals(expected, responseEntity.getBody()),
                () -> {
                    if (responseEntity.getBody() != null) {
                        assertTrue(responseEntity.getBody().getLinks().stream()
                                .allMatch(link -> link.getHref().contains(authorsUrl)));
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
    void getOrderByIdShouldReturnInvalidRequest() throws JsonProcessingException {
        long id = -2L;
        URI uri = UriComponentsBuilder.fromUriString("/v1/api/order").build().encode().toUri();
        String expected = JsonMapper.builder().build().writeValueAsString(
                Map.of("errorCode", ErrorConstants.ORDER_INVALID_REQUEST_ERROR_CODE,
                        "errorMessage", "Invalid input ( id = " + id
                                + " ). Only a positive number is allowed ( 1 and more )."));
        requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(uri + "/{id}", HttpMethod.GET, requestEntity,
                        new ParameterizedTypeReference<>() {
                        }, id);

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
    void getOrderByIdShouldThrowNotFound() throws JsonProcessingException {
        long id = 103L;
        URI uri = UriComponentsBuilder.fromUriString("/v1/api/order").build().encode().toUri();
        JsonMapper jsonMapper = new JsonMapper();
        String expected = jsonMapper.writeValueAsString(Map.of("errorMessage", ErrorConstants.ORDER_NOT_FOUND_MESSAGE,
                "errorCode", ErrorConstants.ORDER_NOT_FOUND_ERROR_CODE));
        requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(uri + "/{id}", HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {
                }, id);
        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode()),
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
    void createNewOrder() {
        URI uri = UriComponentsBuilder.fromUriString("/v1/api/order").build().encode().toUri();
        final String authorsUrl = restTemplate.getRootUri();
        Tag tag1 = Tag.builder().id(1L).name("testTag1").build();
        Tag tag2 = Tag.builder().id(2L).name("testTag2").build();
        Certificate certificate1 = Certificate.builder().id(1L).name("testCertificate1")
                .description("first description").
                durationOfDays("10").price(BigDecimal.valueOf(21050, 2))
                .createDate(LocalDateTime.parse("2022-12-24T12:51:55"))
                .lastUpdateDate(LocalDateTime.parse("2022-12-24T12:51:55")).tags(List.of(tag1, tag2)).build();
        User user = User.builder().id(2L).email("testUser2@mail.com").role(Role.USER).build();
        Order expected = Order.builder().id(3L).totalPrice(certificate1.getPrice()).certificates(List.of(certificate1)).user(user).build();
        HttpEntity<List<Certificate>> request = new HttpEntity<>(List.of(certificate1), headers);
        ResponseEntity<Order> responseEntity =
                restTemplate.exchange(uri, HttpMethod.POST, request, Order.class);
        if (responseEntity.getBody() != null) {
            expected.setPurchaseDate(responseEntity.getBody().getPurchaseDate());
        }

        assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType()),
                () -> assertEquals(expected.getCertificates(), responseEntity.getBody().getCertificates()),
                () -> {
                    if (responseEntity.getBody() != null) {
                        assertTrue(responseEntity.getBody().getLinks().stream()
                                .allMatch(link -> link.getHref().contains(authorsUrl)));
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
    void createNewOrderShouldReturnInvalidRequest() throws JsonProcessingException {
        Certificate certificate = Certificate.builder().build();
        URI uri = UriComponentsBuilder.fromUriString("/v1/api/order").build().encode().toUri();
        String expected = JsonMapper.builder().build().writeValueAsString(
                Map.of("errorMessage", "Invalid certificate field  id ( id = " +
                                certificate.getId() + "). Only a positive number is allowed ( 1 and more ).",
                        "errorCode", ErrorConstants.ORDER_INVALID_REQUEST_ERROR_CODE));
        HttpEntity<List<Certificate>> request = new HttpEntity<>(List.of(certificate), headers);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode()),
                () -> assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType()),
                () -> assertEquals(expected, responseEntity.getBody())
        );
    }
}
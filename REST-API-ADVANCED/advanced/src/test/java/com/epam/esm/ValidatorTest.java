package com.epam.esm;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ValidatorTest {

    @InjectMocks
    private Validator validator;

    @Test
    void shouldReturnValidPageableRequestWhenRequestMoreThenAvailable() {
        long available = 100;
        Pageable request = PageRequest.of(25, 20);
        Pageable expected = PageRequest.of(4, 20);

        assertEquals(expected, validator.validPageableRequest(available, request));
    }

    @Test
    void shouldReturnValidPageableRequestWhenRequestIsOk() {
        long available = 100;
        Pageable request = PageRequest.of(0, 20);
        Pageable expected = PageRequest.of(0, 20);

        assertEquals(expected, validator.validPageableRequest(available, request));
    }

    @Test
    void isPositiveAndParsableIdResponseReturnMessageIfIdSmallerThanOne() {
        String request = "-1";
        assertFalse(validator.isPositiveAndParsableIdResponse(request).isEmpty());
    }

    @Test
    void isPositiveAndParsableIdResponseReturnMessageIfIdInvalid() {
        String request = "errorId";
        assertFalse(validator.isPositiveAndParsableIdResponse(request).isEmpty());
    }

    @Test
    void isPositiveAndParsableIdResponseReturnEmptyStringIfIdOK() {
        String request = "1";
        assertTrue(validator.isPositiveAndParsableIdResponse(request).isEmpty());
    }
}
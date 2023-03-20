package com.epam.esm.order.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {

    @InjectMocks
    private OrderValidator orderValidator;

    @Test
    void isValidOrderFieldsWithErrorResponse() {
        assertEquals("Order must contains certificates",
                orderValidator.isValidOrderFieldsWithErrorResponse(List.of()));
    }
}
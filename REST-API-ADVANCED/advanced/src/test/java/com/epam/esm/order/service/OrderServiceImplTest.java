package com.epam.esm.order.service;

import com.epam.esm.certificate.entity.Certificate;
import com.epam.esm.certificate.service.CertificateServiceImpl;
import com.epam.esm.order.entity.Order;
import com.epam.esm.order.exception.OrderNotFoundException;
import com.epam.esm.order.repository.OrderRepository;
import com.epam.esm.order.validation.OrderValidator;
import com.epam.esm.user.entity.User;
import com.epam.esm.user.sevice.UserServiceImpl;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderValidator validator;
    @Mock
    private UserServiceImpl userService;
    @Mock
    private CertificateServiceImpl certificateService;
    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void getAllOrder() {
        long availableAmountOfOrders = 2L;
        Pageable pageable = PageRequest.of(0, 20);
        Order order1 = Order.builder().totalPrice(BigDecimal.valueOf(250))
                .purchaseDate(LocalDateTime.now()).build();
        Order order2 = Order.builder().totalPrice(BigDecimal.valueOf(250))
                .purchaseDate(LocalDateTime.now()).build();
        Page<Order> expected = new PageImpl<>(List.of(order1, order2), pageable, availableAmountOfOrders);

        Mockito.when(orderRepository.count()).thenReturn(availableAmountOfOrders);
        Mockito.when(validator.validPageableRequest(availableAmountOfOrders, pageable)).thenReturn(pageable);
        Mockito.when(orderRepository.findAll(pageable)).thenReturn(expected);

        assertEquals(expected.toList(), orderService.getAllOrder(pageable));
    }

    @Test
    void getAllOrderShouldThrowItemNotFound() {
        long availableAmountOfOrders = 0L;
        Pageable pageable = PageRequest.of(0, 20);
        Page<Order> expected = new PageImpl<>(List.of(), pageable, availableAmountOfOrders);

        Mockito.when(orderRepository.count()).thenReturn(availableAmountOfOrders);
        Mockito.when(validator.validPageableRequest(availableAmountOfOrders, pageable)).thenReturn(pageable);
        Mockito.when(orderRepository.findAll(pageable)).thenReturn(expected);

        assertThrows(OrderNotFoundException.class, () -> orderService.getAllOrder(pageable));
    }

    @Test
    void getOrderById() {
        long requestId = 1L;
        Order expected = Order.builder().totalPrice(BigDecimal.valueOf(250))
                .purchaseDate(LocalDateTime.now()).build();

        Mockito.when(orderRepository.findById(requestId)).thenReturn(Optional.of(expected));

        assertEquals(expected, orderService.getOrder(requestId));
    }

    @Test
    void getOrderByIdShouldThrowItemNotFound() {
        long requestId = 2L;

        Mockito.when(orderRepository.findById(requestId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.getOrder(requestId));
    }

    @Test
    void createOrder() {
        BigDecimal price = BigDecimal.valueOf(250);
        Certificate certificate = Certificate.builder().id(1L).price(price).build();
        User user = User.builder().id(1L).build();
        Order request = Order.builder().totalPrice(price).
                certificates(List.of(certificate))
                .user(user)
                .purchaseDate(LocalDateTime.now()).build();
        Order response = Order.builder().id(1L).totalPrice(price)
                .certificates(List.of(certificate)).user(user)
                .purchaseDate(LocalDateTime.now()).build();

        Mockito.when(certificateService.findAllById(List.of(1L))).thenReturn(List.of(certificate));
        Mockito.when(userService.getUserById(1L)).thenReturn(user);
        Mockito.when(orderRepository.saveAndFlush(request)).thenReturn(response);

        assertEquals(response, orderService.createOrder(request));
    }
}
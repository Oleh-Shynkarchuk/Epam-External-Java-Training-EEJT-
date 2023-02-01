package com.epam.esm.order.controller;

import com.epam.esm.order.entity.Order;
import com.epam.esm.order.exception.OrderInvalidRequestException;
import com.epam.esm.order.hateoas.OrderHateoasSupport;
import com.epam.esm.order.service.OrderService;
import com.epam.esm.order.validation.OrderValidator;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/api/order")
public class OrderController {

    private final OrderService orderService;
    private final OrderHateoasSupport hateoasSupport;
    private final OrderValidator validator;

    @Autowired
    public OrderController(OrderService orderService,
                           OrderHateoasSupport hateoasSupport,
                           OrderValidator validator) {
        this.orderService = orderService;
        this.hateoasSupport = hateoasSupport;
        this.validator = validator;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<Order>> getAllOrder(@ParameterObject Pageable paginationCriteria) {

        log.debug("Request accepted getAllOrder. " +
                "Pagination request object = " + paginationCriteria.toString());
        List<Order> allOrder = orderService.getAllOrder(paginationCriteria);
        CollectionModel<Order> orderCollectionModel = hateoasSupport.addHateoasSupportToOrderList(allOrder, paginationCriteria);
        log.debug("Send response to client");
        return ResponseEntity.ok(orderCollectionModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable String id) {

        log.debug("Request accepted getOrderById. " +
                "Id request param = " + id);
        String idResponse = validator.isPositiveAndParsableIdResponse(id);
        if (idResponse.isEmpty()) {
            Order serviceOrder = orderService.getOrder(Long.parseLong(id));
            Order orderAndHateoas = hateoasSupport.addHateoasSupportToSingleOrder(serviceOrder);
            log.debug("Send response to client");
            return ResponseEntity.ok(orderAndHateoas);
        } else {
            throw orderInvalidRequestException(idResponse);
        }
    }

    @PostMapping()
    public ResponseEntity<Order> createNewOrder(@RequestBody Order newOrder) {

        log.debug("Request accepted getOrderById. " +
                "new Order object request = " + newOrder.toString());
        String orderResponse = validator.isValidOrderFieldsWithErrorResponse(newOrder);
        if (orderResponse.isEmpty()) {
            Order order = orderService.createOrder(newOrder);
            Order orderAndHateoas = hateoasSupport.addHateoasSupportToSingleOrder(order);
            log.debug("Send response to client");
            return ResponseEntity.ok(orderAndHateoas);
        } else throw orderInvalidRequestException(orderResponse);
    }

    private OrderInvalidRequestException orderInvalidRequestException(String message) {
        log.error(message);
        return new OrderInvalidRequestException(message);
    }
}

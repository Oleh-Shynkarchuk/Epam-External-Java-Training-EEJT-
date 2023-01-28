package com.epam.esm.order.controller;

import com.epam.esm.errorhandle.validation.Validate;
import com.epam.esm.hateoas.HateoasSupport;
import com.epam.esm.order.entity.Order;
import com.epam.esm.order.exception.OrderInvalidRequestException;
import com.epam.esm.order.service.OrderService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/order")
public class OrderController {

    private final OrderService orderService;
    private final HateoasSupport hateoasSupport;
    private final Validate validate;

    @Autowired
    public OrderController(OrderService orderService, HateoasSupport hateoasSupport, Validate validate) {
        this.orderService = orderService;
        this.hateoasSupport = hateoasSupport;
        this.validate = validate;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<Order>> getAllOrder(@ParameterObject Pageable paginationCriteria) {
        return ResponseEntity.ok(
                hateoasSupport.addHateoasSupportToOrderList(
                        orderService.getAllOrder(paginationCriteria),
                        paginationCriteria
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable String id) {
        if (validate.id(id)) {
            return ResponseEntity.ok(
                    hateoasSupport.addHateoasSupportToSingleOrder(orderService.getOrder(Long.parseLong(id)))
            );
        } else throw new OrderInvalidRequestException("Invalid input ( id = " + id
                + " ). Only a positive number is allowed ( 1 and more ).");
    }

    @PostMapping()
    public ResponseEntity<Order> createNewOrder(@RequestBody Order newOrder) {
        return ResponseEntity.ok(
                hateoasSupport.addHateoasSupportToSingleOrder(orderService.createOrder(newOrder))
        );
    }
}

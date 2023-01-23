package com.epam.esm.order.controller;

import com.epam.esm.Validate;
import com.epam.esm.certificate.controller.PaginationCriteria;
import com.epam.esm.order.entity.Order;
import com.epam.esm.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> getAllOrder(PaginationCriteria paginationCriteria) {
        Validate.pagination(paginationCriteria);
        return orderService.getAllOrder(paginationCriteria.getPage(), paginationCriteria.getSize());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return new ResponseEntity<>(orderService.getOrder(id), HttpStatus.OK);
    }

    @PostMapping()
    public Order getOrderById(@RequestBody Order newOrder) {
        return orderService.createOrder(newOrder);
    }
}

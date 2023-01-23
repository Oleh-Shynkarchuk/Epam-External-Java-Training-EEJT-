package com.epam.esm.order.service;

import com.epam.esm.order.entity.Order;

import java.util.List;

public interface OrderService {
    List<Order> getAllOrder(String page, String size);

    Order getOrder(Long id);

    //    List<Order> getAllOrderByUserId(Long id);
    Order createOrder(Order newOrder);
}

package com.epam.esm.order.service;

import com.epam.esm.order.entity.Order;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    List<Order> getAllOrder(Pageable paginationCriteria);

    Order getOrder(Long id);

    Order createOrder(Order newOrder);
}

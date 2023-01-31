package com.epam.esm.order.service;

import com.epam.esm.ErrorConstants;
import com.epam.esm.certificate.entity.Certificate;
import com.epam.esm.certificate.service.CertificateService;
import com.epam.esm.order.entity.Order;
import com.epam.esm.order.exception.OrderNotFoundException;
import com.epam.esm.order.repository.OrderRepository;
import com.epam.esm.order.validation.OrderValidator;
import com.epam.esm.user.sevice.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CertificateService certificateService;
    private final UserService userService;
    private final OrderValidator validator;

    @Autowired
    public OrderServiceImpl(
            OrderRepository orderRepository,
            CertificateService certificateService,
            OrderValidator validator,
            UserService userService) {
        this.orderRepository = orderRepository;
        this.certificateService = certificateService;
        this.validator = validator;
        this.userService = userService;
    }

    @Override
    public List<Order> getAllOrder(Pageable paginationCriteria) {
        log.debug("Start of getAllOrders method in service layer." +
                "For valid non erroneous pageable request get amount of all orders in repository");
        long amountOfOrders = orderRepository.count();

        log.debug("Validate pagination request.");
        Pageable pageable = validator.validPageableRequest(amountOfOrders, paginationCriteria);

        log.debug("Get orders from repository with pagination");
        Page<Order> all = orderRepository.findAll(pageable);

        log.debug("Emptiness check.");
        if (all.isEmpty()) {
            throw orderNotFoundException();
        }
        log.debug("Service return received orders from repository");
        return all.toList();
    }

    @Override
    public Order getOrder(Long id) {

        log.debug("Start of getOrderById method in service layer. " +
                "Get order by id from repository");
        Order order = orderRepository.findById(id).orElseThrow(this::orderNotFoundException);

        log.debug("Service return received order from repository");
        return order;
    }

    @Override
    public Order createOrder(Order newOrder) {

        log.debug("Start of create new order method in service layer." +
                "Set data to order fields");
        BigDecimal totalPrice = new BigDecimal(0);
        newOrder.setCertificates(certificateService.findAllById(getCertificateIdList(newOrder)));
        for (Certificate certificate : newOrder.getCertificates()) {
            totalPrice = totalPrice.add(certificate.getPrice());
        }
        newOrder.setUser(userService.getUserById(newOrder.getUser().getId()));
        newOrder.setTotalPrice(totalPrice);
        newOrder.setPurchaseDate(LocalDateTime.now().toString());

        log.debug("Send new order to repository");
        Order order = orderRepository.saveAndFlush(newOrder);

        log.debug("Service return received new order from repository");
        return order;
    }

    private List<Long> getCertificateIdList(Order newOrder) {
        return newOrder.getCertificates().stream().map(Certificate::getId)
                .collect(Collectors.toList());
    }

    private OrderNotFoundException orderNotFoundException() {
        log.error(ErrorConstants.ORDER_NOT_FOUND_MESSAGE);
        return new OrderNotFoundException();
    }
}

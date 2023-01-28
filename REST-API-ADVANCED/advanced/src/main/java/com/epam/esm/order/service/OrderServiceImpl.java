package com.epam.esm.order.service;

import com.epam.esm.certificate.entity.Certificate;
import com.epam.esm.certificate.service.CertificateService;
import com.epam.esm.errorhandle.constants.ErrorConstants;
import com.epam.esm.errorhandle.validation.Validate;
import com.epam.esm.order.entity.Order;
import com.epam.esm.order.exception.OrderNotFoundException;
import com.epam.esm.order.repository.OrderRepository;
import com.epam.esm.user.sevice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CertificateService certificateService;
    private final UserService userService;
    private final Validate validate;

    @Autowired
    public OrderServiceImpl(
            OrderRepository orderRepository,
            CertificateService certificateService,
            Validate validate,
            UserService userService) {
        this.orderRepository = orderRepository;
        this.certificateService = certificateService;
        this.validate = validate;
        this.userService = userService;
    }

    @Override
    public List<Order> getAllOrder(Pageable paginationCriteria) {
        return orderRepository.findAll(
                validate.validNonErroneousPageableRequest(
                        orderRepository.count(),
                        paginationCriteria
                )
        ).toList();
    }

    @Override
    public Order getOrder(Long id) {
        return orderRepository.findById(id).orElseThrow(
                () -> new OrderNotFoundException(ErrorConstants.ORDER_NOT_FOUND_MESSAGE)
        );
    }

    @Override
    public Order createOrder(Order newOrder) {
        validate.order(newOrder);
        BigDecimal totalPrice = new BigDecimal(0);
        newOrder.setCertificates(certificateService.findAllById(getCertificateIdList(newOrder)));
        for (Certificate certificate : newOrder.getCertificates()) {
            totalPrice = totalPrice.add(certificate.getPrice());
        }
        newOrder.setUser(userService.getUserById(newOrder.getUser().getId()));
        newOrder.setTotalPrice(totalPrice);
        newOrder.setPurchaseDate(LocalDateTime.now().toString());
        return orderRepository.saveAndFlush(newOrder);
    }

    private List<Long> getCertificateIdList(Order newOrder) {
        return newOrder.getCertificates().stream().map(Certificate::getId)
                .collect(Collectors.toList());
    }
}

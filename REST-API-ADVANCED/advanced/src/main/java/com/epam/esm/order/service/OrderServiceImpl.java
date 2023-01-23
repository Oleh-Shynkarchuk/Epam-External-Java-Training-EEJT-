package com.epam.esm.order.service;

import com.epam.esm.certificate.entity.Certificate;
import com.epam.esm.certificate.service.CertificateService;
import com.epam.esm.order.entity.Order;
import com.epam.esm.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CertificateService certificateService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, CertificateService certificateService) {
        this.orderRepository = orderRepository;
        this.certificateService = certificateService;
    }

    @Override
    public List<Order> getAllOrder(String page, String size) {
        if (page != null && size != null) {
            return orderRepository.findAll(PageRequest.of(Integer.parseInt(page), Integer.parseInt(size))).toList();
        } else return orderRepository.findAll();
    }

    @Override
    public Order getOrder(Long id) {
        return orderRepository.findById(id).orElseThrow();
    }

    @Override
    public Order createOrder(Order newOrder) {
        BigDecimal bigDecimal = new BigDecimal(0);
        List<Certificate> certificateList = new ArrayList<>();
        for (Certificate c : newOrder.getCertificates()) {
            Optional<Certificate> get = certificateService.getCertificateByName(c);
            if (get.isPresent()) {
                bigDecimal = bigDecimal.add(get.get().getPrice());
                certificateList.add(get.get());
            }
        }
        newOrder.setCertificates(certificateList);
        newOrder.setTotalPrice(bigDecimal);
        newOrder.setPurchaseDate(LocalDateTime.now().toString());
//        return orderRepository.saveAndFlush(newOrder);
        System.out.println(newOrder);
        return orderRepository.saveAndFlush(newOrder);
    }
}

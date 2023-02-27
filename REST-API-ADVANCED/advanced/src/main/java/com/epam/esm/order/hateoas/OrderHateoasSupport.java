package com.epam.esm.order.hateoas;

import com.epam.esm.certificate.hateoas.CertificateHateoasSupport;
import com.epam.esm.order.controller.OrderController;
import com.epam.esm.order.entity.Order;
import com.epam.esm.user.hateoas.UserHateoasSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderHateoasSupport {
    private final CertificateHateoasSupport hateoasSupport;
    private final UserHateoasSupport userHateoasSupport;

    @Autowired
    public OrderHateoasSupport(CertificateHateoasSupport certificateHateoasSupport, UserHateoasSupport userHateoasSupport) {
        this.hateoasSupport = certificateHateoasSupport;
        this.userHateoasSupport = userHateoasSupport;
    }

    public Order addHateoasSupportToSingleOrder(Order order) {
        hateoasSupport.addHateoasSupportToCertificateList(order.getCertificates(), Pageable.unpaged());
        userHateoasSupport.addHateoasSupportToSingleUser(order.getUser());
        return order.add(
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class)
                        .getOrderById(String.valueOf(order.getId()))).withRel("getOrder"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class)
                        .getAllOrder(Pageable.unpaged())).withRel("getAllOrders"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class).
                        createNewOrder(List.of())).withRel("createNewOrder")
        );
    }

    public CollectionModel<Order> addHateoasSupportToOrderList(List<Order> allOrder, Pageable paginationCriteria) {
        allOrder.forEach(this::addHateoasSupportToOrder);
        allOrder.forEach(order -> userHateoasSupport.addHateoasSupport(order.getUser()));
        allOrder.forEach(order -> hateoasSupport.addHateoasSupportToCertificateList(order.getCertificates(), Pageable.unpaged()));
        return CollectionModel.of(allOrder).add(
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class)
                        .getAllOrder(paginationCriteria)).withRel("getAllOrders"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class)
                        .createNewOrder(List.of())).withRel("createNewOrder")
        );
    }

    public void addHateoasSupportToOrder(Order order) {
        if (order.getLinks().isEmpty()) {
            order.add(
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class)
                            .getOrderById(String.valueOf(order.getId()))).withRel("getOrder")
            );
        }
    }
}

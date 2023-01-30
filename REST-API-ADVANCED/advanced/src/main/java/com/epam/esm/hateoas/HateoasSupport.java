package com.epam.esm.hateoas;

import com.epam.esm.certificate.controller.CertificateController;
import com.epam.esm.certificate.entity.Certificate;
import com.epam.esm.order.controller.OrderController;
import com.epam.esm.order.entity.Order;
import com.epam.esm.tag.controller.TagController;
import com.epam.esm.tag.entity.Tag;
import com.epam.esm.user.controller.UserController;
import com.epam.esm.user.entity.User;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.List;

@Configuration
public class HateoasSupport {

    public CollectionModel<Certificate> addHateoasSupportToCertificateList(
            List<Certificate> allCertificate,
            Pageable paginationCriteria
    ) {
        return addHateoasSupport(allCertificate, paginationCriteria, List.of());
    }

    public CollectionModel<Certificate> addHateoasSupport(
            List<Certificate> allCertificate,
            Pageable paginationCriteria,
            List<String> tagName
    ) {
        allCertificate.forEach(this::addHateoasSupportToCertificate);
        allCertificate.forEach(certificate -> certificate.getTags().forEach(this::addHateoasSupportToTag));
        return CollectionModel.of(allCertificate).add(
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CertificateController.class)
                        .getAllGiftCertificates(paginationCriteria)).withRel("getAllCertificates"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CertificateController.class)
                                .getCertificateBySeveralTagsName(paginationCriteria, tagName))
                        .withRel("getCertificateBySeveralTagsName"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CertificateController.class)
                        .createCertificate(Certificate.builder().build())).withRel("createNewCertificate"));
    }

    public Certificate addHateoasSupportToSingleCertificate(Certificate certificate) {
        addHateoasSupportToCertificate(certificate);
        certificate.getTags().forEach(this::addHateoasSupportToTag);
        return certificate.add(
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CertificateController.class)
                        .getAllGiftCertificates(Pageable.unpaged())).withRel("getAllCertificates"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CertificateController.class)
                                .getCertificateBySeveralTagsName(Pageable.unpaged(), List.of()))
                        .withRel("getCertificateBySeveralTagsName"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CertificateController.class)
                        .createCertificate(certificate)).withRel("createNewCertificate")
        );
    }

    public void addHateoasSupportToCertificate(Certificate certificate) {
        if (certificate.getLinks().isEmpty()) {
            certificate.add(
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CertificateController.class)
                                    .getCertificateById(String.valueOf(certificate.getId())))
                            .withRel("getCertificate"),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CertificateController.class)
                                    .deleteCertificateById(String.valueOf(certificate.getId())))
                            .withRel("deleteCertificate"),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CertificateController.class)
                                    .patchCertificate(String.valueOf(certificate.getId()), certificate))
                            .withRel("patchCertificate")
            );
        }
    }

    public void addHateoasSupportToTag(Tag t) {
        if (t.getLinks().isEmpty()) {
            t.add(
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                            .getTagById(String.valueOf(t.getId()))).withRel("getTag"),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                            .deleteTag(String.valueOf(t.getId()))).withRel("deleteTag")
            );
        }
    }

    public Order addHateoasSupportToSingleOrder(Order order) {
        addHateoasSupportToCertificateList(order.getCertificates(), Pageable.unpaged());
        addHateoasSupport(order.getUser());
        return order.add(
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class)
                        .getOrderById(String.valueOf(order.getId()))).withRel("getOrder"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class)
                        .getAllOrder(Pageable.unpaged())).withRel("getAllOrders"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class).
                        createNewOrder(Order.builder().build())).withRel("createNewOrder")
        );
    }

    public CollectionModel<Order> addHateoasSupportToOrderList(List<Order> allOrder, Pageable paginationCriteria) {
        allOrder.forEach(this::addHateoasSupportToOrder);
        allOrder.forEach(order -> addHateoasSupport(order.getUser()));
        allOrder.forEach(order -> addHateoasSupportToCertificateList(order.getCertificates(), Pageable.unpaged()));
        return CollectionModel.of(allOrder).add(
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class)
                        .getAllOrder(paginationCriteria)).withRel("getAllOrders"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class)
                        .createNewOrder(Order.builder().build())).withRel("createNewOrder")
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

    public CollectionModel<User> addHateoasSupportToUserList(List<User> allUser, Pageable paginationCriteria) {
        allUser.forEach(this::addHateoasSupport);
        return CollectionModel.of(allUser).add(
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                        .getAllUser(paginationCriteria)).withRel("getAllUser")
        );
    }

    public User addHateoasSupportToSingleUser(User user) {
        return user.add(
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                        .getUser(String.valueOf(user.getId()))).withRel("getUser"));
    }

    public void addHateoasSupport(User user) {
        if (user.getLinks().isEmpty()) {
            user.add(
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                            .getUser(String.valueOf(user.getId()))).withRel("getUser"));
        }
    }

    public Tag addHateoasSupportToSingleTag(Tag t) {
        return t.add(
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                        .getTagById(String.valueOf(t.getId()))).withRel("getTag"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                        .getMostWidelyUsedTag()).withRel("getMostWidelyUsedTag"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                        .getAllTag(Pageable.unpaged())).withRel("getAllTag"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                        .createTag(Tag.builder().build())).withRel("createNewTag"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                        .deleteTag(String.valueOf(t.getId()))).withRel("deleteTag")
        );
    }

    public CollectionModel<Tag> addHateoasSupportToAllTag(List<Tag> allTag, @ParameterObject Pageable paginationCriteria) {
        allTag.forEach(this::addHateoasSupportToTag);
        return CollectionModel.of(allTag).add(
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                        .getAllTag(paginationCriteria)).withRel("getAllTag"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                        .getMostWidelyUsedTag()).withRel("getMostWidelyUsedTag"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                        .createTag(Tag.builder().build())).withRel("createNewTag")
        );
    }
}

package com.epam.esm.certificate.hateoas;

import com.epam.esm.certificate.controller.EcommerceCertificateController;
import com.epam.esm.certificate.entity.GCertificate;
import com.epam.esm.tag.hateoas.TagHateoasSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class GCertificateHateoasSupport {

    private final TagHateoasSupport tagHateoasSupport;

    @Autowired
    public GCertificateHateoasSupport(TagHateoasSupport tagHateoasSupport) {
        this.tagHateoasSupport = tagHateoasSupport;
    }

    public CollectionModel<GCertificate> addHateoasSupportToCertificateList(
            List<GCertificate> allCertificate,
            Pageable paginationCriteria
    ) {
        return addHateoasSupport(allCertificate, paginationCriteria, List.of());
    }

    public CollectionModel<GCertificate> addHateoasSupport(
            List<GCertificate> allCertificate,
            Pageable paginationCriteria,
            List<String> tagName
    ) {
        allCertificate.forEach(this::addHateoasSupportToCertificate);
        allCertificate.forEach(certificate -> {
            if (!CollectionUtils.isEmpty(certificate.getTags())) {
                certificate.getTags()
                        .forEach(tagHateoasSupport::addHateoasSupportToTag);
            }
        });
        return CollectionModel.of(allCertificate).add(
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EcommerceCertificateController.class)
                        .getAllGiftCertificates(paginationCriteria)).withRel("getAllCertificates"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EcommerceCertificateController.class)
                                .getCertificateBySeveralTagsName(paginationCriteria, tagName))
                        .withRel("getCertificateBySeveralTagsName"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EcommerceCertificateController.class)
                        .createCertificate(GCertificate.builder().build())).withRel("createNewCertificate"));
    }

    public GCertificate addHateoasSupportToSingleCertificate(GCertificate certificate) {
        addHateoasSupportToCertificate(certificate);
        if (!CollectionUtils.isEmpty(certificate.getTags())) {
            certificate.getTags().forEach(tagHateoasSupport::addHateoasSupportToTag);
        }
        return certificate.add(
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EcommerceCertificateController.class)
                        .getAllGiftCertificates(Pageable.unpaged())).withRel("getAllCertificates"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EcommerceCertificateController.class)
                                .getCertificateBySeveralTagsName(Pageable.unpaged(), List.of()))
                        .withRel("getCertificateBySeveralTagsName"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EcommerceCertificateController.class)
                        .createCertificate(certificate)).withRel("createNewCertificate")
        );
    }

    public void addHateoasSupportToCertificate(GCertificate certificate) {
        if (certificate.getLinks().isEmpty()) {
            certificate.add(
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EcommerceCertificateController.class)
                                    .getCertificateById(String.valueOf(certificate.getId())))
                            .withRel("getCertificate"),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EcommerceCertificateController.class)
                                    .deleteCertificateById(String.valueOf(certificate.getId())))
                            .withRel("deleteCertificate"),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EcommerceCertificateController.class)
                                    .patchCertificate(String.valueOf(certificate.getId()), certificate))
                            .withRel("patchCertificate")
            );
        }
    }
}

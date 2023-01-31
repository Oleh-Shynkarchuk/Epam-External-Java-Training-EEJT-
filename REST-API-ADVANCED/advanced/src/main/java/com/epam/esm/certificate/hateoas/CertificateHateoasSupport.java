package com.epam.esm.certificate.hateoas;

import com.epam.esm.certificate.controller.CertificateController;
import com.epam.esm.certificate.entity.Certificate;
import com.epam.esm.tag.hateoas.TagHateoasSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.List;

@Configuration
public class CertificateHateoasSupport {

    private final TagHateoasSupport tagHateoasSupport;

    @Autowired
    public CertificateHateoasSupport(TagHateoasSupport tagHateoasSupport) {
        this.tagHateoasSupport = tagHateoasSupport;
    }

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
        allCertificate.forEach(certificate -> certificate.getTags().forEach(tagHateoasSupport::addHateoasSupportToTag));
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
        certificate.getTags().forEach(tagHateoasSupport::addHateoasSupportToTag);
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
}

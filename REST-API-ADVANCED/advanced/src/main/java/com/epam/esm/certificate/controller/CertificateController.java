package com.epam.esm.certificate.controller;

import com.epam.esm.Validate;
import com.epam.esm.certificate.entity.Certificate;
import com.epam.esm.certificate.service.CertificateService;
import com.epam.esm.certificate.service.CertificateServiceImpl;
import com.epam.esm.tag.controller.TagController;
import com.epam.esm.tag.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/v1/api/certificate")
public class CertificateController {

    private final CertificateService certificateService;

    @Autowired
    public CertificateController(CertificateServiceImpl certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<Certificate>> getAllGiftCertificates(PaginationCriteria paginationCriteria) {
        Validate.pagination(paginationCriteria);
        return ResponseEntity.ok(
                addHATEOASSupport(
                        certificateService.getAllCertificate(paginationCriteria),
                        paginationCriteria
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Certificate> getCertificateById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                addHATEOASSupportToSingleCertificate(
                        certificateService.getCertificateById(id)
                )
        );
    }

    @GetMapping("/search")
    public ResponseEntity<CollectionModel<Certificate>> getCertificateBySeveralTagsName(
            PaginationCriteria paginationCriteria,
            @RequestParam List<String> tagName
    ) {
        Validate.pagination(paginationCriteria);
        return ResponseEntity.ok(
                addHATEOASSupport(
                        certificateService.getCertificateByTagsName(paginationCriteria, tagName),
                        PaginationCriteria.builder().build(),
                        tagName
                )
        );
    }

    @PostMapping
    public ResponseEntity<Certificate> createCertificate(@RequestBody Certificate newCertificate) {
        return ResponseEntity.ok(
                addHATEOASSupportToSingleCertificate(
                        certificateService.createCertificate(newCertificate)
                )
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Certificate> patchCertificate(@PathVariable("id") Long id, @RequestBody Certificate patchCertificate) {
        return ResponseEntity.ok(
                addHATEOASSupportToSingleCertificate(
                        certificateService.patchCertificate(id, patchCertificate)
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity.HeadersBuilder<?> deleteCertificateById(@PathVariable("id") Long id) {
        certificateService.deleteCertificateById(id);
        return ResponseEntity.noContent();
    }

    private CollectionModel<Certificate> addHATEOASSupport(
            List<Certificate> allCertificate, PaginationCriteria paginationCriteria
    ) {
        return addHATEOASSupport(allCertificate, paginationCriteria, List.of());
    }

    private CollectionModel<Certificate> addHATEOASSupport(List<Certificate> allCertificate, PaginationCriteria paginationCriteria, List<String> s) {
        allCertificate.forEach(this::addHATEOASSupportToCertificate);

        allCertificate.forEach(certificate -> certificate.getTags().forEach(this::addHATEOASSupportToTag));

        CollectionModel<Certificate> collectionModel = CollectionModel.of(allCertificate);

        collectionModel.add(linkTo(methodOn(CertificateController.class)
                .getAllGiftCertificates(paginationCriteria))
                .withRel("getAllCertificates"));
        collectionModel.add(linkTo(methodOn(CertificateController.class)
                .getCertificateBySeveralTagsName(paginationCriteria, s))
                .withRel("getCertificateBySeveralTagsName"));

        return collectionModel;
    }

    private Certificate addHATEOASSupportToSingleCertificate(Certificate certificate) {
        addHATEOASSupportToCertificate(certificate);

        certificate.getTags().forEach(this::addHATEOASSupportToTag);

        certificate.add(linkTo(methodOn(CertificateController.class)
                .getAllGiftCertificates(PaginationCriteria.builder().build()))
                .withRel("getAllCertificates"));
        certificate.add(linkTo(methodOn(CertificateController.class)
                .getCertificateBySeveralTagsName(PaginationCriteria.builder().build(), List.of()))
                .withRel("getCertificateBySeveralTagsName"));

        return certificate;
    }

    private void addHATEOASSupportToCertificate(Certificate certificate) {
        certificate.add(linkTo(methodOn(CertificateController.class).getCertificateById(certificate.getId()))
                .withRel("getCertificate"));
        certificate.add(linkTo(methodOn(CertificateController.class).deleteCertificateById(certificate.getId()))
                .withRel("deleteCertificate"));
        certificate.add(linkTo(methodOn(CertificateController.class).patchCertificate(certificate.getId(), certificate))
                .withRel("patchCertificate"));
    }

    private void addHATEOASSupportToTag(Tag t) {
        if (t.getLinks().isEmpty()) {
            t.add(linkTo(methodOn(TagController.class).getTagById(t.getId())).withRel("getTag"));
            t.add(linkTo(methodOn(TagController.class).deleteTag(t.getId())).withRel("deleteTag"));
        }
    }
}

package com.epam.esm.certificate.controller;

import com.epam.esm.certificate.entity.Certificate;
import com.epam.esm.certificate.exception.CertificateInvalidRequestException;
import com.epam.esm.certificate.service.CertificateService;
import com.epam.esm.certificate.service.CertificateServiceImpl;
import com.epam.esm.errorhandle.validation.Validator;
import com.epam.esm.hateoas.HateoasSupport;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/api/certificate")
public class CertificateController {

    private final CertificateService certificateService;
    private final HateoasSupport hateoasSupport;
    private final Validator validator;

    @Autowired
    public CertificateController(
            CertificateServiceImpl certificateService,
            HateoasSupport hateoasSupport,
            Validator validator) {
        this.certificateService = certificateService;
        this.hateoasSupport = hateoasSupport;
        this.validator = validator;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<Certificate>> getAllGiftCertificates(
            @ParameterObject Pageable paginationCriteria) {

        log.debug("Request accepted getAllGiftCertificates. Get all certificates from service.");
        List<Certificate> allCertificate = certificateService.getAllCertificates(paginationCriteria);

        log.debug("Add hateoas to certificates.");
        CollectionModel<Certificate> allCertificatesAndHateoas = hateoasSupport
                .addHateoasSupportToCertificateList(allCertificate, paginationCriteria);

        log.debug("Response to client.");
        return ResponseEntity.ok(allCertificatesAndHateoas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Certificate> getCertificateById(
            @PathVariable("id") String id) {
        log.debug("Request accepted getCertificateById. Validate id field.");
        if (validator.isPositiveAndParsableId(id)) {

            log.debug("Get certificate by id from service.");
            Certificate certificateById = certificateService.getCertificateById(Long.parseLong(id));

            log.debug("Add hateoas to certificate.");
            Certificate certificateByIdANDHateoas = hateoasSupport.addHateoasSupportToSingleCertificate(certificateById);

            log.debug("Response to client.");
            return ResponseEntity.ok(certificateByIdANDHateoas);
        } else throw certificateInvalidRequestException(id);
    }

    @GetMapping("/search")
    public ResponseEntity<CollectionModel<Certificate>> getCertificateBySeveralTagsName(
            @ParameterObject Pageable pageable,
            @RequestParam List<String> tagName) {
        log.debug("Request accepted getCertificateBySeveralTagsName. Get certificates from service.");
        List<Certificate> certificateByTagsName = certificateService.getCertificateByTagsName(pageable, tagName);

        log.debug("Add hateoas to certificates.");
        CollectionModel<Certificate> certificatesAndHateoas = hateoasSupport.
                addHateoasSupport(certificateByTagsName, pageable, tagName);

        log.debug("Response to client.");
        return ResponseEntity.ok(certificatesAndHateoas);
    }

    @PostMapping
    public ResponseEntity<Certificate> createCertificate(@RequestBody Certificate newCertificate) {
        log.debug("Request accepted createCertificate. Create new certificate by service.");
        Certificate certificate = certificateService.createCertificate(newCertificate);

        log.debug("Add hateoas to created certificate.");
        Certificate certificateAndHateoas = hateoasSupport.addHateoasSupportToSingleCertificate(certificate);

        log.debug("Response to client.");
        return ResponseEntity.ok(certificateAndHateoas);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Certificate> patchCertificate(
            @PathVariable("id") String id,
            @RequestBody Certificate patchCertificate) {

        log.debug("Request accepted getCertificateById. Validate id field.");
        if (validator.isPositiveAndParsableId(id)) {

            log.debug("Update certificate.");
            Certificate certificate = certificateService.patchCertificate(Long.parseLong(id), patchCertificate);

            log.debug("Add hateoas to updated certificate.");
            Certificate certificateAndHateoas = hateoasSupport.addHateoasSupportToSingleCertificate(certificate);

            log.debug("Response to client.");
            return ResponseEntity.ok(certificateAndHateoas);
        } else throw certificateInvalidRequestException(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCertificateById(
            @PathVariable("id") String id) {

        log.debug("Request accepted deleteCertificateById. Validate id field.");
        if (validator.isPositiveAndParsableId(id)) {

            log.debug("Delete certificate.");
            certificateService.deleteCertificateById(Long.parseLong(id));

            log.debug("Response to client.");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        throw certificateInvalidRequestException(id);
    }

    private CertificateInvalidRequestException certificateInvalidRequestException(String id) {
        log.error("Invalid input ( id = " + id + " ). Only a positive number is allowed ( 1 and more ).");
        return new CertificateInvalidRequestException("Invalid input ( id = " + id
                + " ). Only a positive number is allowed ( 1 and more ).");
    }
}

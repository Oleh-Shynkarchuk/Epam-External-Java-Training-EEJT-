package com.epam.esm.certificate.controller;

import com.epam.esm.certificate.entity.Certificate;
import com.epam.esm.certificate.exception.CertificateInvalidRequestException;
import com.epam.esm.certificate.hateoas.CertificateHateoasSupport;
import com.epam.esm.certificate.service.CertificateService;
import com.epam.esm.certificate.service.CertificateServiceImpl;
import com.epam.esm.certificate.validation.CertificateValidator;
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
    private final CertificateHateoasSupport hateoasSupport;
    private final CertificateValidator validator;

    @Autowired
    public CertificateController(
            CertificateServiceImpl certificateService,
            CertificateHateoasSupport hateoasSupport,
            CertificateValidator validator) {
        this.certificateService = certificateService;
        this.hateoasSupport = hateoasSupport;
        this.validator = validator;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<Certificate>> getAllGiftCertificates(
            @ParameterObject Pageable paginationCriteria) {
        log.debug("Request accepted getAllGiftCertificates. " +
                "Pagination details = " + paginationCriteria.toString());
        List<Certificate> allCertificate = certificateService.getAllCertificates(paginationCriteria);
        CollectionModel<Certificate> allCertificatesAndHateoas = hateoasSupport
                .addHateoasSupportToCertificateList(allCertificate, paginationCriteria);
        log.debug("Response to client.");
        return ResponseEntity.ok(allCertificatesAndHateoas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Certificate> getCertificateById(
            @PathVariable("id") String id) {
        log.debug("Request accepted getCertificateById. " +
                "Request id = " + id);
        String idResponse = validator.isPositiveAndParsableIdResponse(id);
        if (idResponse.isEmpty()) {
            Certificate certificateById = certificateService.getCertificateById(Long.parseLong(id));
            Certificate certificateByIdANDHateoas = hateoasSupport.addHateoasSupportToSingleCertificate(certificateById);
            log.debug("Response to client.");
            return ResponseEntity.ok(certificateByIdANDHateoas);
        } else throw certificateInvalidRequestException(idResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<CollectionModel<Certificate>> getCertificateBySeveralTagsName(
            @ParameterObject Pageable pageable,
            @RequestParam List<String> tagName) {
        log.debug("Request accepted getCertificateBySeveralTagsName. " +
                "Pagination details = " + pageable.toString() +
                "Tag name request list : " + tagName.toString());
        List<Certificate> certificateByTagsName = certificateService.
                getCertificateByTagsName(pageable, tagName);
        CollectionModel<Certificate> certificatesAndHateoas = hateoasSupport.
                addHateoasSupport(certificateByTagsName, pageable, tagName);
        log.debug("Response to client.");
        return ResponseEntity.ok(certificatesAndHateoas);
    }

    @PostMapping
    public ResponseEntity<Certificate> createCertificate(@RequestBody Certificate newCertificate) {
        log.debug("Request accepted createCertificate." +
                "newCertificate object request : " + newCertificate.toString());
        String certificateResponse = validator.isCreatableCertificateFieldsWithErrorResponse(newCertificate);
        if (certificateResponse.isEmpty()) {
            Certificate certificate = certificateService.createCertificate(newCertificate);
            Certificate certificateAndHateoas = hateoasSupport.addHateoasSupportToSingleCertificate(certificate);
            log.debug("Response to client.");
            return ResponseEntity.ok(certificateAndHateoas);
        } else throw certificateInvalidRequestException(certificateResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Certificate> patchCertificate(
            @PathVariable("id") String id,
            @RequestBody Certificate patchCertificate) {

        log.debug("Request accepted getCertificateById. " +
                "Id request = " + id +
                "updatedCertificate object request : " + patchCertificate.toString());
        String idResponse = validator.isPositiveAndParsableIdResponse(id);
        String certificateResponse = validator.isUpdatableCertificateFieldsWithErrorResponse(patchCertificate);
        if ((idResponse + certificateResponse).isEmpty()) {
            Certificate certificate = certificateService.patchCertificate(Long.parseLong(id), patchCertificate);
            Certificate certificateAndHateoas = hateoasSupport.addHateoasSupportToSingleCertificate(certificate);
            log.debug("Response to client.");
            return ResponseEntity.ok(certificateAndHateoas);
        } else throw certificateInvalidRequestException(idResponse + certificateResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCertificateById(
            @PathVariable("id") String id) {

        log.debug("Request accepted deleteCertificateById." +
                "Id request = " + id);
        String idResponse = validator.isPositiveAndParsableIdResponse(id);
        if (idResponse.isEmpty()) {
            certificateService.deleteCertificateById(Long.parseLong(id));
            log.debug("Response to client.");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        throw certificateInvalidRequestException(idResponse);
    }

    private CertificateInvalidRequestException certificateInvalidRequestException(String message) {
        log.error(message);
        return new CertificateInvalidRequestException(message);
    }
}

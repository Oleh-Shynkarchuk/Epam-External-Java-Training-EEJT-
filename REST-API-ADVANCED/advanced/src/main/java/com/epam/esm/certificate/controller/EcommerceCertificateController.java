package com.epam.esm.certificate.controller;

import com.epam.esm.certificate.entity.GCertificate;
import com.epam.esm.certificate.exception.CertificateInvalidRequestException;
import com.epam.esm.certificate.hateoas.GCertificateHateoasSupport;
import com.epam.esm.certificate.service.EcommerceCertificateService;
import com.epam.esm.certificate.validation.CertificateValidator;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/v2/api/certificate")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EcommerceCertificateController {

    private final EcommerceCertificateService ecommerceCertificateService;
    private final GCertificateHateoasSupport hateoasSupport;
    private final CertificateValidator validator;

    @GetMapping
    public ResponseEntity<CollectionModel<GCertificate>> getAllGiftCertificates(
            @ParameterObject Pageable paginationCriteria) {
        log.debug("Request accepted getAllGiftCertificates. " +
                "Pagination details = " + paginationCriteria.toString());
        List<GCertificate> allCertificate = ecommerceCertificateService.getAllCertificates(paginationCriteria);
        CollectionModel<GCertificate> allCertificatesAndHateoas = hateoasSupport
                .addHateoasSupportToCertificateList(allCertificate, paginationCriteria);
        log.debug("Response Certificates:" + allCertificate.toString());
        return ResponseEntity.ok(allCertificatesAndHateoas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GCertificate> getCertificateById(
            @PathVariable("id") String id) {
        log.debug("Request accepted getCertificateById. " +
                "Request id = " + id);
        String idResponse = validator.isPositiveAndParsableIdResponse(id);
        if (idResponse.isEmpty()) {
            GCertificate certificateById = ecommerceCertificateService.getCertificateById(id);
            return ResponseEntity.ok(hateoasSupport.addHateoasSupportToSingleCertificate(certificateById));
        }
        throw certificateInvalidRequestException(idResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<CollectionModel<GCertificate>> getCertificateBySeveralTagsName(
            @ParameterObject Pageable pageable,
            @RequestParam List<String> tagName) {
        log.debug("Request accepted getCertificateBySeveralTagsName. " +
                "Pagination details = " + pageable.toString() +
                "Tag name request list : " + tagName.toString());
        List<GCertificate> certificateByTagsName = ecommerceCertificateService.
                getCertificateByTagsName(pageable, tagName);
        return ResponseEntity.ok(hateoasSupport.addHateoasSupport(certificateByTagsName, pageable, tagName));
    }

    @PostMapping
    public ResponseEntity<GCertificate> createCertificate(@RequestBody GCertificate newCertificate) {
        log.debug("Request accepted createCertificate." +
                "newCertificate object request : " + newCertificate.toString());
        String certificateResponse = validator.isCreatableGCertificateFieldsWithErrorResponse(newCertificate);
        if (certificateResponse.isEmpty()) {
            GCertificate certificate = ecommerceCertificateService.createCertificate(newCertificate);
            log.debug("Response to client.");
            return ResponseEntity.ok(hateoasSupport.addHateoasSupportToSingleCertificate(certificate));
        }
        throw certificateInvalidRequestException(certificateResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GCertificate> patchCertificate(
            @PathVariable("id") String id,
            @RequestBody GCertificate patchCertificate) {

        log.debug("Request accepted getCertificateById. " +
                "Id request = " + id +
                "updatedCertificate object request : " + patchCertificate.toString());
        String idResponse = validator.isPositiveAndParsableIdResponse(id);
        String certificateResponse = validator.isUpdatableGCertificateFieldsWithErrorResponse(patchCertificate);
        if ((idResponse + certificateResponse).isEmpty()) {
            GCertificate certificate = ecommerceCertificateService.patchCertificate(id, patchCertificate);
            return ResponseEntity.ok(hateoasSupport.addHateoasSupportToSingleCertificate(certificate));
        }
        throw certificateInvalidRequestException(idResponse + certificateResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCertificateById(
            @PathVariable("id") String id) {
        log.debug("Request accepted deleteCertificateById." +
                "Id request = {}", id);
        String idResponse = validator.isPositiveAndParsableIdResponse(id);
        if (idResponse.isEmpty()) {
            ecommerceCertificateService.deleteCertificateById(id);
            log.debug("Successful delete.");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        throw certificateInvalidRequestException(idResponse);
    }

    private CertificateInvalidRequestException certificateInvalidRequestException(String message) {
        log.error(message);
        return new CertificateInvalidRequestException(message);
    }
}

package com.epam.esm.certificate.controller;

import com.epam.esm.certificate.entity.Certificate;
import com.epam.esm.certificate.exception.CertificateInvalidRequestException;
import com.epam.esm.certificate.service.CertificateService;
import com.epam.esm.certificate.service.CertificateServiceImpl;
import com.epam.esm.errorhandle.validation.Validate;
import com.epam.esm.hateoas.HateoasSupport;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/certificate")
public class CertificateController {

    private final CertificateService certificateService;
    private final HateoasSupport hateoasSupport;
    private final Validate validate;

    @Autowired
    public CertificateController(
            CertificateServiceImpl certificateService,
            HateoasSupport hateoasSupport,
            Validate validate) {
        this.certificateService = certificateService;
        this.hateoasSupport = hateoasSupport;
        this.validate = validate;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<Certificate>> getAllGiftCertificates(
            @ParameterObject Pageable paginationCriteria) {
        return ResponseEntity.ok(
                hateoasSupport.addHateoasSupportToCertificateList(
                        certificateService.getAllCertificate(paginationCriteria),
                        paginationCriteria
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Certificate> getCertificateById(
            @PathVariable("id") String id) {
        if (validate.id(id)) {
            return ResponseEntity.ok(
                    hateoasSupport.addHateoasSupportToSingleCertificate(
                            certificateService.getCertificateById(Long.parseLong(id))
                    )
            );
        } else throw throwExceptionWhenWrongIdInput(id);
    }

    @GetMapping("/search")
    public ResponseEntity<CollectionModel<Certificate>> getCertificateBySeveralTagsName(
            @ParameterObject Pageable pageable,
            @RequestParam List<String> tagName) {
        return ResponseEntity.ok(
                hateoasSupport.addHateoasSupport(
                        certificateService.getCertificateByTagsName(pageable, tagName).toList(),
                        pageable,
                        tagName
                )
        );
    }

    @PostMapping
    public ResponseEntity<Certificate> createCertificate(@RequestBody Certificate newCertificate) {
        return ResponseEntity.ok(
                hateoasSupport.addHateoasSupportToSingleCertificate(
                        certificateService.createCertificate(newCertificate)
                )
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Certificate> patchCertificate(
            @PathVariable("id") String id,
            @RequestBody Certificate patchCertificate) {
        if (validate.id(id)) {
            return ResponseEntity.ok(
                    hateoasSupport.addHateoasSupportToSingleCertificate(
                            certificateService.patchCertificate(Long.parseLong(id), patchCertificate)
                    )
            );
        } else throw throwExceptionWhenWrongIdInput(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCertificateById(
            @PathVariable("id") String id) {
        if (validate.id(id)) {
            certificateService.deleteCertificateById(Long.parseLong(id));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        throw throwExceptionWhenWrongIdInput(id);
    }

    private CertificateInvalidRequestException throwExceptionWhenWrongIdInput(String id) {
        return new CertificateInvalidRequestException("Invalid input ( id = " + id
                + " ). Only a positive number is allowed ( 1 and more ).");
    }
}

package com.epam.esm.certificate.service;

import com.epam.esm.certificate.controller.PaginationCriteria;
import com.epam.esm.certificate.entity.Certificate;

import java.util.List;
import java.util.Optional;

public interface CertificateService {
    List<Certificate> getAllCertificate(PaginationCriteria paginationCriteria);

    Certificate getCertificateById(long id);

    void deleteCertificateById(Long id);

    Certificate createCertificate(Certificate certificate);

    Certificate patchCertificate(Long id, Certificate patchCertificate);

    Optional<Certificate> getCertificateByName(Certificate certificate);

    List<Certificate> getCertificateByTagsName(PaginationCriteria paginationCriteria, List<String> tagName);
}

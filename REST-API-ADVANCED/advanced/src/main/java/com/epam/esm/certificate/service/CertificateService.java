package com.epam.esm.certificate.service;

import com.epam.esm.certificate.entity.Certificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CertificateService {
    List<Certificate> getAllCertificate(Pageable paginationCriteria);

    Certificate getCertificateById(long id);

    void deleteCertificateById(Long id);

    Certificate createCertificate(Certificate certificate);

    Certificate patchCertificate(Long id, Certificate patchCertificate);

    Page<Certificate> getCertificateByTagsName(Pageable pageRequest, List<String> tagName);

    List<Certificate> findAllById(List<Long> idList);
}

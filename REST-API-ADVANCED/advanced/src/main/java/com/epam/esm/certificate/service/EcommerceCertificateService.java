package com.epam.esm.certificate.service;

import com.epam.esm.certificate.entity.GCertificate;
import com.epam.esm.certificate.repo.CertificateEcommerceRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EcommerceCertificateService {

    private final CertificateEcommerceRepo certificateEcommerceRepo;

    public List<GCertificate> getAllCertificates(Pageable paginationCriteria) {
        return certificateEcommerceRepo.getPageableCertificates(paginationCriteria);
    }

    public GCertificate getCertificateById(String id) {
        return certificateEcommerceRepo.getCertificateById(id);
    }


    public void deleteCertificateById(String id) {
        certificateEcommerceRepo.deleteCertificateById(id);
    }

    public GCertificate createCertificate(GCertificate certificate) {
        return certificateEcommerceRepo.createCertificate(certificate);
    }

    public GCertificate patchCertificate(String id, GCertificate patchCertificate) {
        return certificateEcommerceRepo.updateCertificate(id, patchCertificate);
    }

    public List<GCertificate> getCertificateByTagsName(Pageable pageRequest, List<String> tagName) {
        return certificateEcommerceRepo.getCertificateByTagsName(pageRequest, tagName);
    }
}

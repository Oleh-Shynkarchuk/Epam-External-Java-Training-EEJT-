package com.epam.esm.certificate.service;

import com.epam.esm.certificate.entity.Certificate;
import com.epam.esm.certificate.exception.CertificateInvalidRequestException;
import com.epam.esm.certificate.exception.CertificateNotFoundException;
import com.epam.esm.certificate.repo.CertificateRepository;
import com.epam.esm.errorhandle.constants.ErrorConstants;
import com.epam.esm.errorhandle.validation.Validate;
import com.epam.esm.tag.entity.Tag;
import com.epam.esm.tag.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepository;
    private final TagService tagService;
    private final Validate validate;

    @Autowired
    public CertificateServiceImpl(CertificateRepository certificateRepository, TagService tagService, Validate validate) {
        this.certificateRepository = certificateRepository;
        this.tagService = tagService;
        this.validate = validate;
    }

    public List<Certificate> getAllCertificate(Pageable paginationCriteria) {
        return certificateRepository.findAll(
                validate.validNonErroneousPageableRequest(
                        certificateRepository.count(),
                        paginationCriteria
                )
        ).toList();
    }

    @Override
    public Certificate getCertificateById(long id) {
        return certificateRepository.findById(id).orElseThrow(
                () -> new CertificateNotFoundException(ErrorConstants.CERTIFICATE_NOT_FOUND_MESSAGE)
        );
    }

    @Override
    public Certificate createCertificate(Certificate newCertificate) {
        validate.certificate(newCertificate);
        if (certificateIsExist(newCertificate)) {
            throw getRequestException(newCertificate);
        }
        newCertificate.setCreateDate(LocalDateTime.now().toString());
        newCertificate.setLastUpdateDate(LocalDateTime.now().toString());
        newCertificate.setTags(getTagList(newCertificate));
        return certificateRepository.saveAndFlush(newCertificate);
    }

    @Override
    public Certificate patchCertificate(Long id, Certificate patchCertificate) {
        validate.certificate(patchCertificate);
        if (certificateIsExist(id, patchCertificate)) {
            throw getRequestException(patchCertificate);
        }
        patchCertificate = certificateRepository.findById(id).orElseThrow(
                () -> new CertificateNotFoundException(ErrorConstants.CERTIFICATE_NOT_FOUND_MESSAGE)
        ).merge(patchCertificate);
        patchCertificate.setTags(getTagList(patchCertificate));
        patchCertificate.setLastUpdateDate(LocalDateTime.now().toString());
        return certificateRepository.saveAndFlush(patchCertificate);
    }

    @Override
    public void deleteCertificateById(Long id) {
        if (certificateRepository.existsById(id)) {
            certificateRepository.deleteById(id);
        } else throw new CertificateNotFoundException(ErrorConstants.CERTIFICATE_NOT_FOUND_MESSAGE);
    }

    @Override
    public Page<Certificate> getCertificateByTagsName(Pageable pageRequest, List<String> tagName) {
        return certificateRepository.findByTagsNameAndPagination(tagName, (long) tagName.size(), pageRequest);
    }

    @Override
    public List<Certificate> findAllById(List<Long> idList) {
        return certificateRepository.findAllById(idList);
    }

    private List<Tag> getTagList(Certificate updateCertificate) {
        List<Tag> list = new ArrayList<>();
        if (updateCertificate.getTags() != null) {
            for (Tag tag : updateCertificate.getTags()) {
                if (tagService.existByName(tag.getName())) {
                    list.add(tagService.getTagByName(tag.getName()));
                } else {
                    list.add(tagService.createTag(tag));
                }
            }
        }
        return list;
    }

    private boolean certificateIsExist(Certificate newCertificate) {
        return certificateRepository.existsByName(newCertificate.getName());
    }

    private boolean certificateIsExist(Long id, Certificate patchCertificate) {
        return certificateIsExist(patchCertificate) &&
                !certificateRepository.findByName(patchCertificate.getName()).getId().equals(id);
    }

    private CertificateInvalidRequestException getRequestException(Certificate patchCertificate) {
        return new CertificateInvalidRequestException("Certificate with ( name =  " + patchCertificate.getName()
                + ") already exist. This field must be unique, change it and try again.");
    }
}

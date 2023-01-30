package com.epam.esm.certificate.service;

import com.epam.esm.certificate.entity.Certificate;
import com.epam.esm.certificate.exception.CertificateInvalidRequestException;
import com.epam.esm.certificate.exception.CertificateNotFoundException;
import com.epam.esm.certificate.repo.CertificateRepository;
import com.epam.esm.errorhandle.constants.ErrorConstants;
import com.epam.esm.errorhandle.validation.Validator;
import com.epam.esm.tag.entity.Tag;
import com.epam.esm.tag.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepository;
    private final TagService tagService;
    private final Validator validator;

    @Autowired
    public CertificateServiceImpl(CertificateRepository certificateRepository, TagService tagService, Validator validator) {
        this.certificateRepository = certificateRepository;
        this.tagService = tagService;
        this.validator = validator;
    }

    public List<Certificate> getAllCertificates(Pageable paginationCriteria) {
        log.debug("Start of get all certificates method in service layer." +
                "For valid non erroneous pageable request get amount of all certificates in repository");
        long countOfCertificates = certificateRepository.count();

        log.debug("Validate pagination request.");
        Pageable pageable = validator.validPageableRequest(countOfCertificates, paginationCriteria);

        log.debug("Get certificates from repository with pagination");
        Page<Certificate> all = certificateRepository.findAll(pageable);

        log.debug("Emptiness check.");
        if (all.isEmpty()) {
            throw getCertificateNotFoundException();
        }
        log.debug("Service send received certificates from repository");
        return all.toList();
    }

    @Override
    public Certificate getCertificateById(long id) {

        log.debug("Start of getCertificateById method in service layer. " +
                "Get certificate by id from repository");
        Certificate certificate = certificateRepository.findById(id).
                orElseThrow(this::getCertificateNotFoundException);

        log.debug("Service send received certificate from repository");
        return certificate;
    }

    @Override
    @Transactional
    public Certificate createCertificate(Certificate newCertificate) {
        log.debug("Start of create new certificate method in service layer." +
                "Validate new certificate fields");
        validator.certificate(newCertificate);

        log.debug("Uniqueness check");
        if (certificateIsExist(newCertificate)) {
            throw getInvalidRequestException(newCertificate);
        }
        log.debug("Set valid data before creation.");
        newCertificate.setId(null);
        newCertificate.setCreateDate(LocalDateTime.now().toString());
        newCertificate.setLastUpdateDate(LocalDateTime.now().toString());
        newCertificate.setTags(getActualTagList(newCertificate));

        log.debug("Saving a new certificate in repository.");
        Certificate savedCertificate = certificateRepository.saveAndFlush(newCertificate);

        log.debug("Return saved certificate from repository");
        return savedCertificate;
    }

    @Override
    @Transactional
    public Certificate patchCertificate(Long id, Certificate patchCertificate) {
        log.debug("Start of patch certificate method in service layer." +
                "Validate updated certificate fields");
        validator.certificate(patchCertificate);

        log.debug("Uniqueness check.");
        if (StringUtils.isNotEmpty(patchCertificate.getName())) {
            if (certificateIsExist(id, patchCertificate)) {
                throw getInvalidRequestException(patchCertificate);
            }
        }
        log.debug("Get a certificate by id request parameter or else throw error." +
                "After this merge with updated version of certificate");
        patchCertificate = certificateRepository.findById(id).
                orElseThrow(this::getCertificateNotFoundException)
                .merge(patchCertificate);

        log.debug("Set valid data before updating.");
        patchCertificate.setTags(getActualTagList(patchCertificate));
        patchCertificate.setLastUpdateDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        log.debug("Saving updated version of certificate in repository.");
        Certificate certificate = certificateRepository.saveAndFlush(patchCertificate);

        log.debug("Return saved certificate from repository");
        return certificate;
    }

    @Override
    public void deleteCertificateById(Long id) {
        log.debug("Start of delete method by id in service layer." +
                "Existence check");
        if (certificateRepository.existsById(id)) {

            log.debug("Deleting certificate from repository");
            certificateRepository.deleteById(id);
            log.debug("Successful!");

        } else throw getCertificateNotFoundException();
    }

    @Override
    public List<Certificate> getCertificateByTagsName(Pageable pageRequest, List<String> tagName) {

        log.debug("Start of get certificate by several tags name method in service layer." +
                "Get certificates from repository");
        Page<Certificate> certificates = certificateRepository.
                findByTagsNameAndPagination(tagName, (long) tagName.size(), pageRequest);

        log.debug("Emptiness check.");
        if (certificates.isEmpty()) {
            throw getCertificateNotFoundException();
        }
        log.debug("Service send received data from repository");
        return certificates.toList();
    }

    @Override
    public List<Certificate> findAllById(List<Long> idList) {
        log.debug("Start of get certificates by several id method in service layer." +
                "Get certificates from repository");
        List<Certificate> allById = certificateRepository.findAllById(idList);

        log.debug("If at least one certificate is not found, get an error.");
        if (allById.size() != idList.size()) {
            throw getCertificateNotFoundException();
        }

        log.debug("Service send received data from repository");
        return allById;
    }

    private List<Tag> getActualTagList(Certificate updateCertificate) {

        log.debug("Save new tags or select existing ones");
        List<Tag> list = new ArrayList<>();
        log.debug("Emptiness check.");
        if (!CollectionUtils.isEmpty(updateCertificate.getTags())) {
            for (Tag tag : updateCertificate.getTags()) {
                log.debug("Uniqueness check");
                if (tagService.existByName(tag.getName())) {
                    log.debug("Select an existing one from repository");
                    list.add(tagService.getTagByName(tag.getName()));
                } else {
                    log.debug("Save new tag into repository");
                    list.add(tagService.createTag(tag));
                }
            }
        }
        log.debug("return tag list for certificate");
        return list;
    }

    private boolean certificateIsExist(Certificate newCertificate) {
        return certificateRepository.existsByName(newCertificate.getName());
    }

    private boolean certificateIsExist(Long id, Certificate patchCertificate) {
        return certificateIsExist(patchCertificate) &&
                !certificateRepository.findByName(patchCertificate.getName()).getId().equals(id);
    }

    private CertificateInvalidRequestException getInvalidRequestException(Certificate patchCertificate) {
        log.error("Certificate with ( name =  " + patchCertificate.getName()
                + ") already exist. This field must be unique, change it and try again.");
        return new CertificateInvalidRequestException("Certificate with ( name =  " + patchCertificate.getName()
                + ") already exist. This field must be unique, change it and try again.");
    }

    private CertificateNotFoundException getCertificateNotFoundException() {
        log.error(ErrorConstants.CERTIFICATE_NOT_FOUND_MESSAGE);
        return new CertificateNotFoundException();
    }
}

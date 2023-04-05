package com.epam.esm.certificate.service;

import com.epam.esm.ErrorConstants;
import com.epam.esm.certificate.entity.Certificate;
import com.epam.esm.certificate.exception.CertificateInvalidRequestException;
import com.epam.esm.certificate.exception.CertificateNotFoundException;
import com.epam.esm.certificate.repo.CertificateRepository;
import com.epam.esm.certificate.validation.CertificateValidator;
import com.epam.esm.tag.entity.Tag;
import com.epam.esm.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepository;
    private final TagService tagService;
    private final CertificateValidator validator;

    public List<Certificate> getAllCertificates(Pageable paginationCriteria) {
        log.debug("Start of get all certificates method in service layer." +
                "For valid non erroneous pageable request get amount of all certificates in repository");
        long countOfCertificates = certificateRepository.count();
        Pageable pageable = validator.validPageableRequest(countOfCertificates, paginationCriteria);
        Page<Certificate> all = certificateRepository.findAll(pageable);
        log.debug("Emptiness check.");
        if (all.isEmpty()) {
            throw getCertificateNotFoundException();
        }
        return all.toList();
    }

    @Override
    public Certificate getCertificateById(long id) {
        log.debug("Start of getCertificateById method in service layer. " +
                "Get certificate by id from repository");
        return certificateRepository.findById(id).
                orElseThrow(this::getCertificateNotFoundException);
    }

    @Override
    @Transactional
    public Certificate createCertificate(Certificate newCertificate) {
        log.debug("Start of create new certificate method in service layer." +
                "Uniqueness check");
        if (certificateIsExist(newCertificate)) {
            throw certificateInvalidRequestException(newCertificate);
        }
        newCertificate.setId(null);
        newCertificate.setTags(getActualTagList(newCertificate));
        log.debug("Saving a new certificate in repository.");
        return certificateRepository.saveAndFlush(newCertificate);
    }

    @Override
    @Transactional
    @Modifying
    public Certificate patchCertificate(Long id, Certificate patchCertificate) {
        log.debug("Start of patch certificate method in service layer." +
                "Uniqueness check.");
        if (StringUtils.isNotEmpty(patchCertificate.getName())
                && certificateIsExist(id, patchCertificate)) {
            throw certificateInvalidRequestException(patchCertificate);
        }
        log.debug("Get a certificate by id request parameter or else throw error." +
                "After this merge with updated version of certificate");
        patchCertificate = certificateRepository.findById(id).
                orElseThrow(this::getCertificateNotFoundException)
                .merge(patchCertificate);
        patchCertificate.setTags(getActualTagList(patchCertificate));
        log.debug("Saving updated version of certificate in repository.");
        return certificateRepository.saveAndFlush(patchCertificate);
    }

    @Override
    public void deleteCertificateById(Long id) {
        log.debug("Start of delete method by id in service layer." +
                "Existence check");
        if (certificateRepository.existsById(id)) {
            log.debug("Deleting certificate from repository");
            certificateRepository.deleteById(id);
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
        return allById;
    }

    private List<Tag> getActualTagList(Certificate updateCertificate) {
        log.debug("Save new tags or select existing ones");
        List<Tag> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(updateCertificate.getTags())) {
            for (Tag tag : updateCertificate.getTags()) {
                log.debug("Select an existing one from repository or save new tag into repository." +
                        "Tag object =" + tag);
                Optional<Tag> tagByName = tagService.getTagByName(tag.getName());
                tagByName.ifPresent(list::add);
                if (tagByName.isEmpty()) {
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

    private CertificateInvalidRequestException certificateInvalidRequestException(Certificate patchCertificate) {
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

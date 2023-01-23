package com.epam.esm.certificate.service;

import com.epam.esm.certificate.controller.PaginationCriteria;
import com.epam.esm.certificate.entity.Certificate;
import com.epam.esm.certificate.repo.CertificateRepository;
import com.epam.esm.tag.entity.Tag;
import com.epam.esm.tag.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepository;
    private final TagService tagService;

    @Autowired
    public CertificateServiceImpl(CertificateRepository certificateRepository, TagService tagService) {
        this.certificateRepository = certificateRepository;
        this.tagService = tagService;
    }

    public List<Certificate> getAllCertificate(PaginationCriteria paginationCriteria) {
        if (paginationCriteria.getPage() != null && paginationCriteria.getSize() != null) {
            return certificateRepository.findAll(PageRequest.of(Integer.parseInt(paginationCriteria.getPage()), Integer.parseInt(paginationCriteria.getSize()))).toList();
        } else return certificateRepository.findAll();
    }

    @Override
    public Certificate getCertificateById(long id) {
        return certificateRepository.findById(id).orElseThrow();
    }

    @Override
    public Certificate createCertificate(Certificate newCertificate) {
        newCertificate.setCreateDate(LocalDateTime.now().toString());
        newCertificate.setLastUpdateDate(LocalDateTime.now().toString());
        newCertificate.setTags(getTagList(newCertificate));
        return certificateRepository.saveAndFlush(newCertificate);
    }

    @Override
    public Certificate patchCertificate(Long id, Certificate patchCertificate) {
        patchCertificate = certificateRepository.findById(id).orElseThrow().merge(patchCertificate);
        patchCertificate.setTags(getTagList(patchCertificate));
        patchCertificate.setLastUpdateDate(LocalDateTime.now().toString());
        return certificateRepository.saveAndFlush(patchCertificate);
    }

    @Override
    public void deleteCertificateById(Long id) {
        certificateRepository.deleteById(id);
    }

    @Override
    public Optional<Certificate> getCertificateByName(Certificate certificate) {
        return certificateRepository.findOne(Example.of(Certificate.builder().name(certificate.getName()).build()));
    }

    public List<Certificate> getCertificateByTagsName(PaginationCriteria paginationCriteria, List<String> tagName) {
        return certificateRepository.findByTagsNameAndPagination(
                tagName,
                (long) tagName.size(),
                PageRequest.of(
                        Integer.parseInt(paginationCriteria.getPage()),
                        Integer.parseInt(paginationCriteria.getSize())
                )
        );
    }

    private List<Tag> getTagList(Certificate updateCertificate) {
        List<Tag> list = new ArrayList<>();
        if (updateCertificate.getTags() != null) {
            for (Tag tag : updateCertificate.getTags()) {
                Optional<Tag> tagByName = tagService.findTagByName(tag.getName());
                if (tagByName.isPresent()) {
                    list.add(tagByName.get());
                } else {
                    list.add(tagService.createTag(tag));
                }
            }
        }
        return list;
    }
}

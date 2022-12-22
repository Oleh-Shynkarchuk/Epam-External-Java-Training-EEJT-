package com.epam.esm.giftcertificates.service;

import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.repo.GiftCertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GiftCertificateServiceImpl implements GiftCertificatesService {
    @Autowired
    GiftCertificateRepository giftCertificateRepository;//TODO

    @Override
    public Optional<GiftCertificate> read(String sqlQuery) {
        return giftCertificateRepository.getGiftCertificateByTagName(sqlQuery);
    }

    @Override
    public List<GiftCertificate> read() {
        return giftCertificateRepository.getAllGiftCertificates();
    }

    @Override
    public Optional<GiftCertificate> create(GiftCertificate createGiftCertificate) {
        List<GiftCertificate> allGiftCertificates = giftCertificateRepository.getAllGiftCertificates();
        if (allGiftCertificates.stream().anyMatch(giftCertificate -> giftCertificate.getName().equals(createGiftCertificate.getName()))) {
        return Optional.empty();
        }
        return giftCertificateRepository.createNewGiftCertificate(createGiftCertificate);
    }

    @Override
    public boolean delete(Long id) {
        return giftCertificateRepository.deleteGiftCertificateById(id);
    }


    @Override
    public Optional<GiftCertificate> read(Long id) {
        return giftCertificateRepository.getGiftCertificateById(id);
    }

    @Override
    public boolean update(Long id, GiftCertificate updateGiftCertificate) {
        return giftCertificateRepository.updateGiftCertificateById(id, updateGiftCertificate);
    }
}

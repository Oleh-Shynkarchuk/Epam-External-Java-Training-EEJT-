package com.epam.esm.giftcertificates.repo;

import com.epam.esm.giftcertificates.entity.GiftCertificate;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateRepository {
    Optional<GiftCertificate> getGiftCertificateById(Long id);

    List<GiftCertificate> getAllGiftCertificates();

    Optional<GiftCertificate> createNewGiftCertificate(GiftCertificate giftCertificate);

    boolean deleteGiftCertificateById(Long id);

    boolean updateGiftCertificateById(Long id, GiftCertificate giftCertificate);

    Optional<GiftCertificate> getGiftCertificateByTagName(String name);
}

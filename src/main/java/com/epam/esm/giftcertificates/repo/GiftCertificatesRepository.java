package com.epam.esm.giftcertificates.repo;

import com.epam.esm.giftcertificates.entity.GiftCertificate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GiftCertificatesRepository {
    Optional<GiftCertificate> getGiftCertificateById(Long id);

    Optional<List<GiftCertificate>> getAllGiftCertificates();

    Optional<GiftCertificate> createNewGiftCertificate(GiftCertificate giftCertificate);

    boolean deleteGiftCertificateById(Long id);

    Optional<GiftCertificate> updateGiftCertificateById(Long id, GiftCertificate giftCertificate);

    Optional<List<GiftCertificate>> getGiftCertificateByParam(String statementQuery,List<String>paramList);

    boolean isGiftCertificateByNameExist(String name);
}

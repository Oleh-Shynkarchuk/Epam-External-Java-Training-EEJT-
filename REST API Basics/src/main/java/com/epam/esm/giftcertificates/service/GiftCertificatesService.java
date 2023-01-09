package com.epam.esm.giftcertificates.service;

import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.filter.entity.SearchParams;

import java.util.List;

public interface GiftCertificatesService {
    List<GiftCertificate> readAllGiftCertificates();
    GiftCertificate createGiftCertificate(GiftCertificate newGiftCertificate);
    void deleteGiftCertificate(Long id);
    GiftCertificate updateGiftCertificate(Long id, GiftCertificate newGiftCertificate);

    GiftCertificate readGiftCertificate(Long id);

    List<GiftCertificate> readGiftCertificate(SearchParams searchParams);
}

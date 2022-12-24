package com.epam.esm.giftcertificates.service;

import com.epam.esm.giftcertificates.entity.GiftCertificate;

import java.util.List;

public interface GiftCertificatesService {
    List<GiftCertificate> read();
    GiftCertificate create(GiftCertificate newGiftCertificate);
    boolean delete(Long id);
    GiftCertificate update(Long id, GiftCertificate newGiftCertificate);

    GiftCertificate read(Long id);

    List<GiftCertificate> read(String tagName, String name, String description, String sortByDate, String sortByName);
}

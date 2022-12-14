package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;

public interface GiftCertificateService {
    List<GiftCertificate> read();
    GiftCertificate create(GiftCertificate newGiftCertificate);
    GiftCertificate delete(Long id);
    GiftCertificate update(Long id,GiftCertificate newGiftCertificate);
}

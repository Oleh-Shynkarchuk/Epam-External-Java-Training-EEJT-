package com.epam.esm.giftcertificates.service;

import com.epam.esm.giftcertificates.entity.GiftCertificate;

import java.util.List;
import java.util.Optional;

public interface GiftCertificatesService {
    List<GiftCertificate> read();
    boolean create(GiftCertificate newGiftCertificate);
    boolean delete(Long id);
    boolean update(Long id, GiftCertificate newGiftCertificate);

    Optional<GiftCertificate> read(Long id);

    Optional<GiftCertificate> read(String sql);
}

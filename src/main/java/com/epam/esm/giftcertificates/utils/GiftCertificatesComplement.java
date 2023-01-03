package com.epam.esm.giftcertificates.utils;

import com.epam.esm.giftcertificates.entity.GiftCertificate;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;


public class GiftCertificatesComplement {
    public static GiftCertificate complementCertificateOnUpdateByCertificateFromDB(GiftCertificate updateGiftCertificate, GiftCertificate certificateFromDB) {
        if (StringUtils.isEmpty(updateGiftCertificate.getName())) {
         updateGiftCertificate.setName(certificateFromDB.getName());   
        }
        if (StringUtils.isEmpty(updateGiftCertificate.getDescription())) {
            updateGiftCertificate.setDescription(certificateFromDB.getDescription());
        }
        if (StringUtils.isEmpty(updateGiftCertificate.getDuration())) {
            updateGiftCertificate.setDuration(String.valueOf(Duration.parse(certificateFromDB.getDuration()).toDays()));
        }
        if (updateGiftCertificate.getPrice()==null) {
            updateGiftCertificate.setPrice(certificateFromDB.getPrice());
        }
        return updateGiftCertificate;
    }
}

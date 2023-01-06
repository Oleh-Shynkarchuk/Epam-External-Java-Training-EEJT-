package com.epam.esm.giftcertificates.validation;

import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.exception.CertificateInvalidRequest;

import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

public class GiftValidate {
    private static final String ASC = "ASC";
    private static final String DESC = "DESC";

    public static void positiveRequestedId(Long id) {
        if (id <= 0L)
            throw new CertificateInvalidRequest("Invalid id ( request id = " + id
                    + " ). ID must be positive number.", HttpStatus.BAD_REQUEST.value() * 100 + 1);
    }

    public static void findGiftByFieldsValidation(String sortByDate, String sortByName, String... ech) {
        if (Arrays.stream(ech).filter(Objects::nonNull).anyMatch(String::isBlank)) {
            throw new CertificateInvalidRequest("Invalid request param. Request param value cant be blank.",
                    HttpStatus.BAD_REQUEST.value() * 100 + 2);
        }
        if (sortByDate != null && (!sortByDate.toUpperCase(Locale.ROOT).equals(ASC) && !sortByDate.toUpperCase(Locale.ROOT).equals(DESC))) {
            throw new CertificateInvalidRequest("Invalid request param sortByDate ( request = " + sortByDate
                    + "). Valid values (ASC,DESC)", HttpStatus.BAD_REQUEST.value() * 100 + 2);
        }
        if (sortByName != null && (!sortByName.toUpperCase(Locale.ROOT).equals(ASC) && !sortByName.toUpperCase(Locale.ROOT).equals(DESC))) {
            throw new CertificateInvalidRequest("Invalid request param sortByName ( request = " + sortByName
                    + "). Valid values (ASC,DESC)", HttpStatus.BAD_REQUEST.value() * 100 + 2);
        }
    }

    public static void GiftCertificateOnUpdate(Long id, GiftCertificate updateGiftCertificate) {
        positiveRequestedId(id);
        if (updateGiftCertificate.getPrice()!=null&&updateGiftCertificate.getPrice().compareTo(BigDecimal.ZERO)<=0) {
            throw new CertificateInvalidRequest("Invalid certificate field price ( price = " + updateGiftCertificate.getPrice()
                    + "). Price must be positive number!", HttpStatus.BAD_REQUEST.value() * 100 + 2);
        }
        if (Integer.parseInt(updateGiftCertificate.getDuration())<0) {
            throw new CertificateInvalidRequest("Invalid certificate field duration ( duration = " + updateGiftCertificate.getDuration()
                    + "). Duration can not be negative time!", HttpStatus.BAD_REQUEST.value() * 100 + 2);
        }
    }
}

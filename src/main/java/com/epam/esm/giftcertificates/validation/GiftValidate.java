package com.epam.esm.giftcertificates.validation;

import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.exception.CertificateInvalidRequest;

import org.apache.commons.lang3.StringUtils;
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
        System.out.println(sortByDate+""+sortByName+""+ Arrays.toString(ech));
        System.out.println(Arrays.stream(ech).allMatch(StringUtils::isEmpty));
        System.out.println(Arrays.stream(ech).noneMatch(StringUtils::isEmpty));
        if (Arrays.stream(ech).allMatch(StringUtils::isEmpty)&&StringUtils.isEmpty(sortByDate)&&StringUtils.isEmpty(sortByName)) {
            throw new CertificateInvalidRequest("Invalid request param. At least one param must be entered.",
                    HttpStatus.BAD_REQUEST.value() * 100 + 2);
        }
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

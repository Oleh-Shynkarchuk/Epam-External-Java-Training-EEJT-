package com.epam.esm.giftcertificates.validation;

import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.exception.CertificateInvalidRequestException;
import com.epam.esm.giftcertificates.filter.entity.SearchParams;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Objects;

public class GiftValidate {
    private static final String ASC = "ASC";
    private static final String DESC = "DESC";

    public static void positiveRequestedId(Long id) {
        if (id <= 0L)
            throw new CertificateInvalidRequestException("Invalid id ( request id = " + id
                    + " ). ID must be positive number.");
    }

    public static void findCertificateByFieldsValidation(SearchParams searchParams) {
        if (searchParams.getAllParams().stream().allMatch(StringUtils::isEmpty)) {
            throw new CertificateInvalidRequestException("Invalid request param. At least one param must be entered.");
        }
        if (searchParams.getAllParams().stream().filter(Objects::nonNull).anyMatch(StringUtils::isBlank)) {
            throw new CertificateInvalidRequestException("Invalid request param. Request param value cant be blank.");
        }
        if (StringUtils.isNotEmpty(searchParams.getSortDate()) && (!searchParams.getSortDate().toUpperCase(Locale.ROOT)
                .equals(ASC) && !searchParams.getSortDate().toUpperCase(Locale.ROOT).equals(DESC))) {
            throw new CertificateInvalidRequestException("Invalid request param sortDate ( request = " +
                    searchParams.getSortDate() + "). Valid values (ASC,DESC)");
        }
        if (StringUtils.isNotEmpty(searchParams.getSortName()) && (!searchParams.getSortName().toUpperCase(Locale.ROOT)
                .equals(ASC) && !searchParams.getSortName().toUpperCase(Locale.ROOT).equals(DESC))) {
            throw new CertificateInvalidRequestException("Invalid request param sortName ( request = " +
                    searchParams.getSortName() + "). Valid values (ASC,DESC)");
        }
    }

    public static void certificateOnUpdate(Long id, GiftCertificate updateGiftCertificate) {
        positiveRequestedId(id);
        certificateFieldValidation(updateGiftCertificate);
    }

    public static void certificateFieldValidation(GiftCertificate updateGiftCertificate) {

        if (StringUtils.isNotEmpty(updateGiftCertificate.getDuration()) && !NumberUtils.isDigits(updateGiftCertificate.getDuration())) {
            throw new CertificateInvalidRequestException("Invalid certificate field  duration ( duration = " + updateGiftCertificate.getDuration()
                    + "). Duration must be positive number!");
        }
        if (updateGiftCertificate.getPrice() != null && updateGiftCertificate.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new CertificateInvalidRequestException("Invalid certificate field price ( price = " + updateGiftCertificate.getPrice()
                    + "). Price must be positive or zero!");
        }
        if (StringUtils.isNotEmpty(updateGiftCertificate.getDuration()) && !(Integer.parseInt(updateGiftCertificate.getDuration()) > 0)) {
            throw new CertificateInvalidRequestException("Invalid certificate field duration ( duration = " + updateGiftCertificate.getDuration()
                    + "). Duration must be positive number!");
        }
    }
}

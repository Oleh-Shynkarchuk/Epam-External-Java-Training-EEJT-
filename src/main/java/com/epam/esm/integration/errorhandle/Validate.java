package com.epam.esm.integration.errorhandle;

import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.tags.entity.Tag;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Validate {
    private static final String ASC = "ASC";
    private static final String DESC = "DESC";

    public static void positiveRequestedId(Long id) {
        if (id <= 0L)
            throw new InvalidRequest("Invalid id ( request id = " + id
                    + " ). ID must be positive number.", HttpStatus.BAD_REQUEST.value() * 100 + 1,HttpStatus.BAD_REQUEST);
    }

    public static void findGiftByFieldsValidation(String sortByDate, String sortByName, String... ech) {
        if (Arrays.stream(ech).filter(Objects::nonNull).anyMatch(String::isBlank)) {
            throw new InvalidRequest("Invalid request param. Request param value cant be blank.",
                    HttpStatus.BAD_REQUEST.value() * 100 + 2, HttpStatus.BAD_REQUEST);
        }
        if (sortByDate != null && (!sortByDate.equals(ASC) && !sortByDate.equals(DESC))) {
            throw new InvalidRequest("Invalid request param sortByDate ( request = " + sortByDate
                    + "). Valid values (ASC,DESC)", HttpStatus.BAD_REQUEST.value() * 100 + 2, HttpStatus.BAD_REQUEST);
        }
        if (sortByName != null && (!sortByName.equals(ASC) && !sortByName.equals(DESC))) {
            throw new InvalidRequest("Invalid request param sortByName ( request = " + sortByName
                    + "). Valid values (ASC,DESC)", HttpStatus.BAD_REQUEST.value() * 100 + 2, HttpStatus.BAD_REQUEST);
        }
    }

    public static void GiftCertificateOnUpdate(Long id, String name, List<GiftCertificate> all) {
        positiveRequestedId(id);
        if (all.stream().noneMatch(giftCertificate -> giftCertificate.getId().equals(id))) {
            throw new InvalidRequest("Gift certificate with id = " + id
                    + " does not exist.",HttpStatus.NOT_FOUND.value()*100+2, HttpStatus.NOT_FOUND);
        }
        FieldNameOfCertificateMustBeUnique(name, all);
    }

    public static void FieldNameOfCertificateMustBeUnique(String name, List<GiftCertificate> all) {
        if (name != null && all.stream().anyMatch(giftCertificate ->
                giftCertificate.getName().equals(name))) {
            throw new InvalidRequest("Gift certificate with name = " + name
                    + " change name field before creating a new certificate",HttpStatus.NOT_FOUND.value()*100+2, HttpStatus.NOT_FOUND);
        }
    }

    public static void FieldNameOfTagMustBeUnique(String name, List<Tag> allTags) {
        if (name != null && allTags.stream().noneMatch(tag -> tag.getName().equals(name))) {
            throw new InvalidRequest("Tag with name = " + name
                    + " change name field before creating a new tag",HttpStatus.NOT_FOUND.value()*100+3, HttpStatus.BAD_REQUEST);
        }
    }
}

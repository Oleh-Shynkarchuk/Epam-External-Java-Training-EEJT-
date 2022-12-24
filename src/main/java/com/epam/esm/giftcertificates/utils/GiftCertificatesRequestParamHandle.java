package com.epam.esm.giftcertificates.utils;

import com.epam.esm.giftcertificates.entity.GiftCertificate;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GiftCertificatesRequestParamHandle {
    public static String createSQLQueryFromReqParam(String tagName, String name, String description, String sortByDate, String sortByName) {
        String baseQuery = "SELECT * FROM gifts.certificates" +
                " JOIN gifts.certificates_has_tags" +
                " ON id=certificates_id JOIN gifts.tags" +
                " ON tags_id=tags.id ";
        if (tagName != null) {
            baseQuery += "WHERE certificates_id IN (select certificates_id" +
                    " FROM gifts.certificates_has_tags" +
                    " JOIN gifts.tags ON tags_id=id WHERE name = ?) ";
        }
        if (name != null) {
            if (tagName == null) {
                baseQuery += "WHERE certificates.name LIKE ? ";
            } else baseQuery += "AND certificates.name LIKE ? ";
        }
        if (description != null) {
            if (tagName == null && name == null) {
                baseQuery += "WHERE description LIKE ? ";
            } else baseQuery += "AND description LIKE ? ";
        }
        if (sortByDate != null) {
            baseQuery += "order by create_date " + sortByDate;
        }
        if (sortByName != null) {
            if (sortByDate == null) {
                baseQuery += "order by certificates.name " + sortByName;
            } else baseQuery += ", certificates.name " + sortByName;
        }
        return baseQuery;
   }

    public static List<String> getParamList(String tagName, String... each) {
        List<String> list = new ArrayList<>();
        if (tagName != null) list.add(tagName);
        list.addAll(Arrays.stream(each).filter(StringUtils::isNotEmpty).map(s -> "%" + s + "%").toList());
        return list;
    }

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

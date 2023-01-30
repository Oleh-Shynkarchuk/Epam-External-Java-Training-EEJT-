package com.epam.esm.errorhandle.validation;

import com.epam.esm.certificate.entity.Certificate;
import com.epam.esm.certificate.exception.CertificateInvalidRequestException;
import com.epam.esm.order.entity.Order;
import com.epam.esm.tag.entity.Tag;
import com.epam.esm.tag.exception.TagInvalidRequestException;
import com.epam.esm.user.exception.UserInvalidRequestException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

@Configuration
public class Validator {

    public Pageable validPageableRequest(long availableAmount, Pageable paginationCriteria) {
        if (availableAmountOfItemsEnoughForPagination(paginationCriteria, availableAmount)) {
            paginationCriteria = PageRequest.of(
                    (int) Math.floor((float) availableAmount / paginationCriteria.getPageNumber()),
                    paginationCriteria.getPageSize()
            );
        }
        return paginationCriteria;
    }

    private boolean availableAmountOfItemsEnoughForPagination(Pageable paginationCriteria, long availableAmount) {
        return (long) paginationCriteria.getPageNumber() * paginationCriteria.getPageSize() > availableAmount;
    }

    public boolean isPositiveAndParsableId(String id) {
        return NumberUtils.isParsable(id) && Long.parseLong(id) > 0;
    }

    public void order(Order newOrder) {
        for (Certificate certificate : newOrder.getCertificates()) {
            if (certificate.getId() == null || certificate.getId() <= 0) {
                throw new CertificateInvalidRequestException("Invalid certificate field  id ( id = "
                        + certificate.getId() + "). Only a positive number is allowed ( 1 and more ).");
            }
        }
        if (newOrder.getUser().getId() == null || newOrder.getUser().getId() <= 0) {
            throw new UserInvalidRequestException("Invalid user field  id ( id = "
                    + newOrder.getUser().getId() + "). Only a positive number is allowed ( 1 and more ).");
        }
    }

    public void certificate(Certificate newCertificate) {
        if (StringUtils.isEmpty(newCertificate.getDurationOfDays()) ||
                !NumberUtils.isDigits(newCertificate.getDurationOfDays())) {
            throw new CertificateInvalidRequestException("Invalid certificate field  duration ( duration = "
                    + newCertificate.getDurationOfDays() + "). Duration must be positive number!");
        }
        if (newCertificate.getPrice() == null ||
                newCertificate.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new CertificateInvalidRequestException("Invalid certificate field price ( price = "
                    + newCertificate.getPrice() + "). Price must be positive or zero!");
        }
        if (StringUtils.isNotEmpty(newCertificate.getDurationOfDays()) &&
                !(Integer.parseInt(newCertificate.getDurationOfDays()) > 0)) {
            throw new CertificateInvalidRequestException("Invalid certificate field duration ( duration = "
                    + newCertificate.getDurationOfDays() + "). Duration must be positive number!");
        }
        if (newCertificate.getTags() != null && !newCertificate.getTags().isEmpty()) {
            newCertificate.getTags().forEach(this::tag);
        }
    }

    public void tag(Tag newTag) {
        if (StringUtils.isEmpty(newTag.getName())) {
            throw new TagInvalidRequestException("Invalid tag field name ( name = "
                    + newTag.getName() + "). Name cannot be empty!");
        }
    }
}

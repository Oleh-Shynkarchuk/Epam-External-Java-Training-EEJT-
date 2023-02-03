package com.epam.esm.certificate.validation;

import com.epam.esm.Validator;
import com.epam.esm.certificate.entity.Certificate;
import com.epam.esm.tag.entity.Tag;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;

@Component
public class CertificateValidator extends Validator {
    public String isCreatableCertificateFieldsWithErrorResponse(Certificate newCertificate) {
        StringBuilder errorStringBuilder = new StringBuilder();
        if (StringUtils.isEmpty(newCertificate.getName()) ||
                StringUtils.isBlank(newCertificate.getName())) {
            errorStringBuilder.append("Invalid certificate field name ( name = ")
                    .append(newCertificate.getName())
                    .append("). Name cannot be empty!");
        }
        if (StringUtils.isEmpty(newCertificate.getDurationOfDays()) ||
                !NumberUtils.isDigits(newCertificate.getDurationOfDays()) ||
                !(Integer.parseInt(newCertificate.getDurationOfDays()) > 0)) {
            errorStringBuilder.append("Invalid certificate field  duration ( duration = ")
                    .append(newCertificate.getDurationOfDays())
                    .append("). Duration for new certificate must be positive number!");
        }
        if (ObjectUtils.isEmpty(newCertificate.getPrice()) ||
                newCertificate.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            errorStringBuilder.append("Invalid certificate field price ( price = ")
                    .append(newCertificate.getPrice())
                    .append("). Price for new certificate must be positive or zero!");
        }
        if (!CollectionUtils.isEmpty(newCertificate.getTags())) {
            newCertificate.getTags().stream()
                    .map(this::isValidTagErrorResponse)
                    .forEach(errorStringBuilder::append);
        }
        return errorStringBuilder.toString();
    }

    public String isUpdatableCertificateFieldsWithErrorResponse(Certificate newCertificate) {
        StringBuilder errorStringBuilder = new StringBuilder();
        if (StringUtils.isNotEmpty(newCertificate.getDurationOfDays())) {
            if (!NumberUtils.isDigits(newCertificate.getDurationOfDays()) ||
                    !(Integer.parseInt(newCertificate.getDurationOfDays()) > 0)) {
                errorStringBuilder.append("Invalid certificate field  duration ( duration = ")
                        .append(newCertificate.getDurationOfDays())
                        .append("). Duration must be positive number!");
            }
        }
        if (newCertificate.getPrice() != null) {
            if (newCertificate.getPrice().compareTo(BigDecimal.ZERO) < 0) {
                errorStringBuilder.append("Invalid certificate field price ( price = ")
                        .append(newCertificate.getPrice())
                        .append("). Price must be positive or zero!");
            }
        }
        if (!CollectionUtils.isEmpty(newCertificate.getTags())) {
            newCertificate.getTags().stream()
                    .map(this::isValidTagErrorResponse)
                    .forEach(errorStringBuilder::append);
        }
        return errorStringBuilder.toString();
    }

    private String isValidTagErrorResponse(Tag newTag) {
        if (StringUtils.isEmpty(newTag.getName()) ||
                StringUtils.isBlank(newTag.getName())) {
            return ("Invalid tag field name ( name = "
                    + newTag.getName() + "). Name cannot be empty!");
        }
        return "";
    }
}

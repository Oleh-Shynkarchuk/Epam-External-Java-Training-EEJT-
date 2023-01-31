package com.epam.esm.certificate.validation;

import com.epam.esm.Validator;
import com.epam.esm.certificate.entity.Certificate;
import com.epam.esm.tag.entity.Tag;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;

@Configuration
public class CertificateValidator extends Validator {
    public String isValidCertificateFieldsWithErrorResponse(Certificate newCertificate) {
        StringBuilder errorStringBuilder = new StringBuilder();
        if (StringUtils.isEmpty(newCertificate.getDurationOfDays()) ||
                !NumberUtils.isDigits(newCertificate.getDurationOfDays()) ||
                !(Integer.parseInt(newCertificate.getDurationOfDays()) > 0)) {
            errorStringBuilder.append("Invalid certificate field  duration ( duration = ")
                    .append(newCertificate.getDurationOfDays())
                    .append("). Duration must be positive number!");
        }
        if (StringUtils.isEmpty(String.valueOf(newCertificate.getPrice())) ||
                newCertificate.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            errorStringBuilder.append("Invalid certificate field price ( price = ")
                    .append(newCertificate.getPrice())
                    .append("). Price must be positive or zero!");
        }
        if (!CollectionUtils.isEmpty(newCertificate.getTags())) {
            newCertificate.getTags().stream()
                    .map(this::isValidTagErrorResponse)
                    .forEach(errorStringBuilder::append);
        }
        return errorStringBuilder.toString();
    }

    private String isValidTagErrorResponse(Tag newTag) {
        if (StringUtils.isEmpty(newTag.getName())) {
            return ("Invalid tag field name ( name = "
                    + newTag.getName() + "). Name cannot be empty!");
        }
        return "";
    }
}

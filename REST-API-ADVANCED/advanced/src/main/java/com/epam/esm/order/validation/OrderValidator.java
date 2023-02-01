package com.epam.esm.order.validation;

import com.epam.esm.Validator;
import com.epam.esm.certificate.entity.Certificate;
import com.epam.esm.order.entity.Order;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OrderValidator extends Validator {
    public String isValidOrderFieldsWithErrorResponse(Order newOrder) {
        StringBuilder errorStringBuilder = new StringBuilder();
        if (CollectionUtils.isEmpty(newOrder.getCertificates())) {
            errorStringBuilder.append("Order must contains certificates");
        }
        for (Certificate certificate : newOrder.getCertificates()) {
            if (StringUtils.isEmpty(String.valueOf(certificate.getId())) || certificate.getId() <= 0) {
                errorStringBuilder.append("Invalid certificate field  id ( id = ")
                        .append(certificate.getId())
                        .append("). Only a positive number is allowed ( 1 and more ).");
            }
        }
        if (ObjectUtils.isEmpty(newOrder.getUser())) {
            errorStringBuilder.append("Invalid user object = ")
                    .append(newOrder.getUser())
                    .append("). Order must contains user data.");
        } else if (UserMustBeEnteredWithID(newOrder)) {
            errorStringBuilder.append("Invalid user field  id ( id = ")
                    .append(newOrder.getUser().getId())
                    .append("). Only a positive number is allowed ( 1 and more ).");
        }
        return errorStringBuilder.toString();
    }

    private boolean UserMustBeEnteredWithID(Order newOrder) {
        return StringUtils.isEmpty(String.valueOf(newOrder.getUser().getId())) ||
                newOrder.getUser().getId() <= 0;
    }
}

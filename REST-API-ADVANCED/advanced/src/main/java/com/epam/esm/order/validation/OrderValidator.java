package com.epam.esm.order.validation;

import com.epam.esm.Validator;
import com.epam.esm.certificate.entity.Certificate;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class OrderValidator extends Validator {
    public String isValidOrderFieldsWithErrorResponse(List<Certificate> newOrder) {
        StringBuilder errorStringBuilder = new StringBuilder();
        if (CollectionUtils.isEmpty(newOrder)) {
            errorStringBuilder.append("Order must contains certificates");
        }
        newOrder.stream().filter(certificate ->
                        ObjectUtils.isEmpty(certificate.getId()) || certificate.getId() <= 0)
                .forEach(certificate -> errorStringBuilder.append("Invalid certificate field  id ( id = ")
                        .append(certificate.getId())
                        .append("). Only a positive number is allowed ( 1 and more )."));
        return errorStringBuilder.toString();
    }
}

package com.epam.esm;


import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;


@Component
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

    public String isPositiveAndParsableIdResponse(String id) {
        if (StringUtils.isEmpty(id) || StringUtils.isBlank(id)) {
            return "Invalid input ( id = " + id + " ).";
        } else return "";
    }

    private boolean availableAmountOfItemsEnoughForPagination(Pageable paginationCriteria, long availableAmount) {
        return (long) paginationCriteria.getPageNumber() * paginationCriteria.getPageSize() > availableAmount;
    }
}

package com.epam.esm;


import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


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

    public String isPositiveAndParsableIdResponse(String id) {
        if (!NumberUtils.isParsable(id) || Long.parseLong(id) <= 0) {
            return "Invalid input ( id = " + id + " ). Only a positive number is allowed ( 1 and more ).";
        } else return "";
    }

    private boolean availableAmountOfItemsEnoughForPagination(Pageable paginationCriteria, long availableAmount) {
        return (long) paginationCriteria.getPageNumber() * paginationCriteria.getPageSize() > availableAmount;
    }
}

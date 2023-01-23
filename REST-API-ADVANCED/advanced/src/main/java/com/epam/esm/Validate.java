package com.epam.esm;

import com.epam.esm.certificate.controller.PaginationCriteria;
import org.apache.commons.lang3.math.NumberUtils;

public class Validate {
    public static void pagination(PaginationCriteria paginationCriteria) {
        boolean check = false;
        String errorMessage = "";
        if (paginationCriteria.getPage() == null) {
            paginationCriteria.setPage("0");
        } else if (!NumberUtils.isParsable(paginationCriteria.getPage())) {
            check = true;
            errorMessage += "Invalid input ( page = '" + paginationCriteria.getPage() + "' ).Valid data (0-9+). ";
        }
        if (paginationCriteria.getSize() == null) {
            paginationCriteria.setSize("5");
        } else if (!NumberUtils.isParsable(paginationCriteria.getSize())) {
            check = true;
            errorMessage += "Invalid input ( size = '" + paginationCriteria.getSize() + "' ).Valid data (0-9+). ";
        }
        if (check) {
            throw new InvalidRequest(errorMessage);
        }
    }

    public static void id(String id) {
        if (!NumberUtils.isParsable(id) || Long.parseLong(id) <= 0) {
            throw new InvalidRequest("Invalid input ( id = " + id + " ).Valid only positive number ( 1 and more ).");
        }
    }
}

package com.epam.esm.tags.validation;


import com.epam.esm.tags.exception.TagInvalidRequestException;
import org.apache.commons.lang3.StringUtils;


public class TagValidate {
    public static void positiveRequestedId(Long id) {
        if (id <= 0L)
            throw new TagInvalidRequestException("Invalid id ( request id = " + id
                    + " ). ID must be positive number.");
    }
    public static void TagFieldValidation(boolean isUnique, String name) {
        if (isUnique) {
            throw new TagInvalidRequestException("Tag with name = " + name
                    + "already exist change name field before creating a new tag");
        }
        if (StringUtils.isEmpty(name)) {
            throw new TagInvalidRequestException("Tag name cannot be empty");
        }
    }
}

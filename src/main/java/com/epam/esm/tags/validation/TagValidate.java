package com.epam.esm.tags.validation;


import com.epam.esm.tags.exception.TagInvalidRequest;
import org.apache.commons.lang3.StringUtils;


public class TagValidate {
    public static void positiveRequestedId(Long id) {
        if (id <= 0L)
            throw new TagInvalidRequest("Invalid id ( request id = " + id
                    + " ). ID must be positive number.");
    }
    public static void TagFieldValidation(boolean isUnique, String name) {
        if (isUnique) {
            throw new TagInvalidRequest("Tag with name = " + name
                    + "already exist change name field before creating a new tag");
        }
        if (StringUtils.isEmpty(name)) {
            throw new TagInvalidRequest("Tag name cannot be empty");
        }
    }
}

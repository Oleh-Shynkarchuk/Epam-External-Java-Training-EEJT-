package com.epam.esm.tag.validation;

import com.epam.esm.Validator;
import com.epam.esm.tag.entity.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class TagValidator extends Validator {

    public String isCreatableTagFieldsErrorResponse(Tag newTag) {
        if (StringUtils.isEmpty(newTag.getName()) ||
                StringUtils.isBlank(newTag.getName())) {
            return ("Invalid tag field name ( name = "
                    + newTag.getName() + "). Name cannot be empty!");
        }
        return "";
    }
}

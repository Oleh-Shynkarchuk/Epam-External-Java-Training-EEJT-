package com.epam.esm.tag.validation;

import com.epam.esm.Validator;
import com.epam.esm.tag.entity.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TagValidator extends Validator {

    public String isValidTagErrorResponse(Tag newTag) {
        if (StringUtils.isEmpty(newTag.getName())) {
            return ("Invalid tag field name ( name = "
                    + newTag.getName() + "). Name cannot be empty!");
        }
        return "";
    }
}

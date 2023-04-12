package com.epam.esm.tag.ecommerceutil;

import com.commercetools.api.models.category.Category;
import com.commercetools.api.models.category.CategoryDraft;
import com.commercetools.api.models.common.LocalizedString;
import com.epam.esm.tag.entity.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class TagUtils {
    public Tag transformCategoryToTag(Category category) {
        return Tag.builder()
                .id(category.getId())
                .name(category.getName().get(Locale.US))
                .version(category.getVersion())
                .build();
    }

    public CategoryDraft transformTagToCategoryDraft(Tag newTag) {
        return CategoryDraft.builder()
                .name(LocalizedString.of(Locale.US, newTag.getName()))
                .slug(LocalizedString.of(Locale.US, StringUtils.deleteWhitespace(newTag.getName() + "1")))
                .orderHint("0.5")
                .build();
    }
}

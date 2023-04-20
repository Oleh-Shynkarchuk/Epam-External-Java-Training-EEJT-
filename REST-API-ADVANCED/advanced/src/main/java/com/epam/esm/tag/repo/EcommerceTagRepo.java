package com.epam.esm.tag.repo;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.category.Category;
import com.commercetools.api.models.category.CategoryDraft;
import com.epam.esm.cache.service.CacheService;
import com.epam.esm.cache.utils.CacheUtil;
import com.epam.esm.tag.ecommerceutil.TagUtils;
import com.epam.esm.tag.entity.Tag;
import com.epam.esm.tag.exception.TagNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EcommerceTagRepo {

    private final ProjectApiRoot projectApiRoot;
    private final TagUtils tagUtils;
    private final CacheService cacheService;

    public List<Tag> getAllTags() {
        return getCategoryFromCache().stream().map(tagUtils::transformCategoryToTag).toList();
    }

    public Tag getTagById(String id) {
        return tagUtils.transformCategoryToTag(
                getCategoryFromCache()
                        .stream()
                        .filter(category -> category.getId().equals(id))
                        .findFirst()
                        .orElseThrow(TagNotFoundException::new));
    }

    public Tag createTag(Tag newTag) {
        CategoryDraft category = tagUtils.transformTagToCategoryDraft(newTag);
        return tagUtils.transformCategoryToTag(createCategory(category));
    }

    public void deleteTag(String id) {
        projectApiRoot.categories()
                .withId(id)
                .delete(getCategoryFromCache()
                        .stream()
                        .filter(category -> category.getId().equals(id))
                        .findFirst()
                        .orElseThrow(TagNotFoundException::new)
                        .getVersion())
                .executeBlocking()
                .getBody();
        getCategoryFromCache().removeIf(category -> category.getId().equals(id));
    }

    public Tag getMost() {
        return null;
    }

    public List<Category> getCategoryFromCache() {
        Object cache = cacheService.getCache(CacheUtil.CATEGORY_CACHE);
        if (ObjectUtils.isEmpty(cache)) {
            cacheService.put(CacheUtil.CATEGORY_CACHE,
                    projectApiRoot
                            .categories()
                            .get()
                            .executeBlocking()
                            .getBody()
                            .getResults()
            );
        }
        return cacheService.getCache(CacheUtil.CATEGORY_CACHE);
    }

    private Category createCategory(CategoryDraft categoryDraft) {
        List<Category> cache = getCategoryFromCache();
        Category category =
                projectApiRoot
                        .categories()
                        .post(categoryDraft)
                        .executeBlocking()
                        .getBody();
        cache.add(category);
        return category;
    }
}

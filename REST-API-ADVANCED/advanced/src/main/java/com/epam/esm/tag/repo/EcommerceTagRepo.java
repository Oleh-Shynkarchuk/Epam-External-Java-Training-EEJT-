package com.epam.esm.tag.repo;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.category.Category;
import com.commercetools.api.models.category.CategoryDraft;
import com.epam.esm.tag.ecommerceutil.TagUtils;
import com.epam.esm.tag.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EcommerceTagRepo {

    private final ProjectApiRoot projectApiRoot;
    private final TagUtils certificateUtils;

    public List<Tag> getAllTags(Pageable paginationCriteria) {
        List<Category> results = projectApiRoot.categories()
                .get()
                .withLimit(paginationCriteria.getPageSize())
                .withOffset(paginationCriteria.getOffset())
                .executeBlocking()
                .getBody().getResults();

        return results.stream().map(certificateUtils::transformCategoryToTag).toList();
    }

    public Tag getTagById(String id) {
        return certificateUtils.transformCategoryToTag(projectApiRoot.categories().withId(id).get().executeBlocking().getBody());
    }

    public Tag createTag(Tag newTag) {
        CategoryDraft category = certificateUtils.transformTagToCategoryDraft(newTag);
        return certificateUtils.transformCategoryToTag(createCategory(category));
    }

    @CacheEvict(cacheNames = "categories", key = "#id")
    public void deleteTag(String id) {
        projectApiRoot.categories().withId(id).delete().executeBlocking().getBody();
    }

    public Tag getMost() {
        return null;
    }

    @Cacheable("categories")
    public List<Category> getAllTags() {
        return projectApiRoot.categories().get().executeBlocking().getBody().getResults();
    }

    @CachePut("categories")
    public Category createCategory(CategoryDraft categoryDraft) {
        return projectApiRoot.categories().post(categoryDraft).executeBlocking().getBody();
    }
}

package com.epam.esm.tag.service;

import com.epam.esm.tag.entity.Tag;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {
    List<Tag> getAllTag(Pageable paginationCriteria);

    Tag getTag(Long id);

    Tag getTagByName(String tagName);

    Tag createTag(Tag newTag);

    void deleteTag(Long id);

    Tag getMostWidelyUsedTag();

    boolean existByName(String name);
}

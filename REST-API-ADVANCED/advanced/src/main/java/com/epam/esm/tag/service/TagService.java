package com.epam.esm.tag.service;

import com.epam.esm.tag.entity.Tag;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TagService {
    List<Tag> getAllTags(Pageable paginationCriteria);

    Tag getTagById(String id);

    Optional<Tag> getTagByName(String tagName);

    Tag createTag(Tag newTag);

    void deleteTagById(String id);

    Tag getMostWidelyUsedTag();

}

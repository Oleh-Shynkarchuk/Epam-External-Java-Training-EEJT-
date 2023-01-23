package com.epam.esm.tag.service;

import com.epam.esm.tag.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagService {
    List<Tag> getAllTag(String page, String size);

    Tag getTag(Long id);

    Optional<Tag> findTagByName(String tagName);

    Tag createTag(Tag newTag);

    void deleteTag(Long id);

    Tag getMostWidelyUsedTag();
}

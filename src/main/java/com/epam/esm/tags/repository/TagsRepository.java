package com.epam.esm.tags.repository;

import com.epam.esm.tags.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagsRepository {
    Optional<Tag> getTagById(Long id);

    List<Tag> getAllTags();
    boolean createNewTag(Tag newTag);
    boolean deleteTagById(Long id);

    Optional<Tag> getTagByName(String name);
}

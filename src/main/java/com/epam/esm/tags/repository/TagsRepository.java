package com.epam.esm.tags.repository;

import com.epam.esm.tags.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagsRepository {
    Optional<Tag> getTagById(Long id);

    Optional<List<Tag>> getAllTags();

    Optional<Tag> createNewTag(Tag newTag);

    boolean deleteTagById(Long id);

    boolean tagByNameExist(String name);
}

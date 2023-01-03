package com.epam.esm.tags.service;

import com.epam.esm.tags.entity.Tag;

import java.util.List;

public interface TagsService {
    Tag readTag(Long id);
    List<Tag> readAllTags();
    Tag createTag(Tag createTag);
    boolean deleteTag(Long id);
}

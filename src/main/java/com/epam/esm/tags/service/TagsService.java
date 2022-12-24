package com.epam.esm.tags.service;

import com.epam.esm.tags.entity.Tag;

import java.util.List;

public interface TagsService {
    Tag read(Long id);
    List<Tag> read();
    Tag create(Tag createTag);
    boolean delete(Long id);
}

package com.epam.esm.tags.service;

import com.epam.esm.tags.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagsService {
    Optional<Tag> read(Long id);
    List<Tag> read();
    boolean create(Tag createTag);
    boolean delete(Long id);

    Optional<Tag> read(String name);
}

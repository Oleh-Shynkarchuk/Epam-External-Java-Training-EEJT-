package com.epam.esm.service;

import com.epam.esm.entity.Tag;

import java.util.List;

public interface TagService {
    List<Tag> read(Long id);
    Tag create(Tag newTag);
    Tag delete(Long id);
}

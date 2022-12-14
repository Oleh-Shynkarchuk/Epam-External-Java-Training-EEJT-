package com.epam.esm.service;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.TagRepository;

import java.util.List;

public class TagServiceImpl implements TagService{
    TagRepository tagRepository;
    @Override
    public List<Tag> read(Long id) {
        return null;
    }

    @Override
    public Tag create(Tag newTag) {
        return null;
    }

    @Override
    public Tag delete(Long id) {
        return null;
    }
}

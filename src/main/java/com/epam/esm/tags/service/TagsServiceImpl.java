package com.epam.esm.tags.service;

import com.epam.esm.tags.entity.Tag;
import com.epam.esm.tags.repository.TagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagsServiceImpl implements TagsService {
    TagsRepository tagsRepository;

    @Autowired
    public void setTagsRepository(TagsRepository tagsRepository) {
        this.tagsRepository = tagsRepository;
    }

    @Override
    public Optional<Tag> read(Long id) {
        return tagsRepository.getTagById(id);
    }

    @Override
    public List<Tag> read() {
        return tagsRepository.getAllTags();
    }

    @Override
    public boolean create(Tag newTag) {
        return tagsRepository.createNewTag(newTag);
    }

    @Override
    public Optional<Tag> read(String name) {
        return tagsRepository.getTagByName(name);
    }

    @Override
    public boolean delete(Long id) {
        return tagsRepository.deleteTagById(id);
    }


}

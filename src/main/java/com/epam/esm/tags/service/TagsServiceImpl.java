package com.epam.esm.tags.service;

import com.epam.esm.integration.errorhandle.Validate;
import com.epam.esm.tags.entity.Tag;
import com.epam.esm.tags.repository.TagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagsServiceImpl implements TagsService {
    private final TagsRepository tagsRepository;

    @Autowired
    public TagsServiceImpl(TagsRepository tagsRepository) {
        this.tagsRepository = tagsRepository;
    }

    @Override
    public Tag readTag(Long id) {
        Validate.positiveRequestedId(id);
        return tagsRepository.getTagById(id).orElseThrow();
    }

    @Override
    public List<Tag> readAllTags() {
        return tagsRepository.getAllTags();
    }

    @Override
    public Tag createTag(Tag newTag) {
        Validate.FieldNameOfTagMustBeUnique(newTag.getName(), (tagsRepository.getAllTags()));
        return tagsRepository.createNewTag(newTag).orElseThrow();
    }

    @Override
    public boolean deleteTag(Long id) {
        readTag(id);
        return tagsRepository.deleteTagById(id);
    }
}

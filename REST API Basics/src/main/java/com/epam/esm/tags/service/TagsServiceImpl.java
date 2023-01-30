package com.epam.esm.tags.service;

import com.epam.esm.tags.entity.Tag;
import com.epam.esm.tags.exception.TagNotFoundException;
import com.epam.esm.tags.exception.TagNotRepresentException;
import com.epam.esm.tags.repository.TagsRepository;
import com.epam.esm.tags.validation.TagValidate;
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
        TagValidate.positiveRequestedId(id);
        return tagsRepository.getTagById(id).orElseThrow(() ->
                new TagNotFoundException("Tag not found where ( id = " + id + " )."
                        + " Try again with different request param"));
    }

    @Override
    public List<Tag> readAllTags() {
        return tagsRepository.getAllTags().orElseThrow(() ->
                new TagNotFoundException("Tags not found."));
    }

    @Override
    public Tag createTag(Tag newTag) {
        TagValidate.TagFieldValidation(tagsRepository.tagByNameExist(newTag.getName()),newTag.getName());
        return tagsRepository.createNewTag(newTag).orElseThrow(() -> new TagNotRepresentException("Can not represent created tag."));
    }

    @Override
    public void deleteTag(Long id) {
        readTag(id);
        if (!tagsRepository.deleteTagById(id)) {
            throw new TagNotRepresentException("Tag has not been deleted.");
        }
    }
}
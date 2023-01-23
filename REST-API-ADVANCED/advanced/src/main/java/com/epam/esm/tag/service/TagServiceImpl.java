package com.epam.esm.tag.service;

import com.epam.esm.tag.entity.Tag;
import com.epam.esm.tag.repo.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Tag> getAllTag(String page, String size) {
        if (page != null && size != null) {
            return tagRepository.findAll(PageRequest.of(Integer.parseInt(page), Integer.parseInt(size))).toList();
        } else return tagRepository.findAll();
    }

    @Override
    public Tag getTag(Long id) {
        return tagRepository.findById(id).orElseThrow();
    }

    public Optional<Tag> findTagByName(String tagName) {
        return tagRepository.findOne(Example.of(Tag.builder().name(tagName).build()));
    }

    @Override
    public Tag createTag(Tag newTag) {
        return tagRepository.saveAndFlush(newTag);
    }

    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    @Override
    public Tag getMostWidelyUsedTag() {
        return getTag(tagRepository.findId());
    }
}

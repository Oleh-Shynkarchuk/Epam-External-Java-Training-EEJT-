package com.epam.esm.tag.service;

import com.epam.esm.errorhandle.constants.ErrorConstants;
import com.epam.esm.errorhandle.validation.Validate;
import com.epam.esm.tag.entity.Tag;
import com.epam.esm.tag.exception.TagNotFoundException;
import com.epam.esm.tag.repo.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final Validate validate;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, Validate validate) {
        this.tagRepository = tagRepository;
        this.validate = validate;
    }

    @Override
    public List<Tag> getAllTag(Pageable paginationCriteria) {
        return tagRepository.findAll(
                validate.validNonErroneousPageableRequest(
                        tagRepository.count(),
                        paginationCriteria
                )
        ).toList();
    }

    @Override
    public Tag getTag(Long id) {
        return tagRepository.findById(id).orElseThrow(
                () -> new TagNotFoundException(ErrorConstants.TAG_NOT_FOUND_MESSAGE)
        );
    }

    @Override
    public boolean existByName(String name) {
        return tagRepository.existsByName(name);
    }

    public Tag getTagByName(String tagName) {
        return tagRepository.getByName(tagName);
    }

    @Override
    public Tag createTag(Tag newTag) {
        return tagRepository.saveAndFlush(newTag);
    }

    @Override
    public void deleteTag(Long id) {
        if (tagRepository.existsById(id)) {
            tagRepository.deleteById(id);
        } else throw new TagNotFoundException(ErrorConstants.TAG_NOT_FOUND_MESSAGE);
    }

    @Override
    public Tag getMostWidelyUsedTag() {
        return getTag(tagRepository.findId());
    }
}

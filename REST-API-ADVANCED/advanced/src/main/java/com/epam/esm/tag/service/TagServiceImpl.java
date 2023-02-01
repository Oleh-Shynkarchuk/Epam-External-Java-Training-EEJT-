package com.epam.esm.tag.service;

import com.epam.esm.ErrorConstants;
import com.epam.esm.tag.entity.Tag;
import com.epam.esm.tag.exception.TagInvalidRequestException;
import com.epam.esm.tag.exception.TagNotFoundException;
import com.epam.esm.tag.repo.TagRepository;
import com.epam.esm.tag.validation.TagValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagValidator validator;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, TagValidator validator) {
        this.tagRepository = tagRepository;
        this.validator = validator;
    }

    @Override
    public List<Tag> getAllTag(Pageable paginationCriteria) {
        log.debug("Start of getAllTag method in service layer." +
                "For valid non erroneous pageable request get amount of all tags in repository");
        long count = tagRepository.count();
        Pageable pageable = validator.validPageableRequest(count, paginationCriteria);
        Page<Tag> allTags = tagRepository.findAll(pageable);
        log.debug("Emptiness check.");
        if (allTags.isEmpty()) {
            throw tagNotFoundException();
        }
        return allTags.toList();
    }

    @Override
    public Tag getTag(Long id) {
        log.debug("Start of getTagById method in service layer. " +
                "Get tag by id from repository");
        return tagRepository.findById(id).orElseThrow(this::tagNotFoundException);
    }

    public Optional<Tag> getTagByName(String tagName) {
        log.debug("Start of existByName method in service layer. " +
                "Get optionalTag by name in repository");
        return tagRepository.findByName(tagName);
    }

    @Override
    public Tag createTag(Tag newTag) {
        log.debug("Start of create new tag method in service layer. " +
                "Save new tag in repository");
        if (tagIsExistByName(newTag.getName())) {
            throw tagInvalidRequestException(newTag);
        }
        return tagRepository.save(newTag);
    }

    @Override
    public void deleteTag(Long id) {
        log.debug("Start of deleteTagById method in service layer. " +
                "Return check result is exist tag by id in repository");
        if (tagRepository.existsById(id)) {
            log.debug("Deleting tag in repository");
            tagRepository.deleteById(id);
        } else throw tagNotFoundException();
    }

    @Override
    public Tag getMostWidelyUsedTag() {
        log.debug("Start of getMostWidelyUsedTag method in service layer. " +
                "Return id of MostWidelyUsedTag in repository");
        Long id = tagRepository.findId();

        log.debug("Get MostWidelyUsedTag in repository by received id");
        return getTag(id);
    }

    private boolean tagIsExistByName(String name) {
        return tagRepository.existsByName(name);
    }

    private TagNotFoundException tagNotFoundException() {
        log.error(ErrorConstants.TAG_NOT_FOUND_MESSAGE);
        return new TagNotFoundException();
    }

    private TagInvalidRequestException tagInvalidRequestException(Tag newTag) {
        log.error("Tag with ( name =  " + newTag.getName()
                + ") already exist. This field must be unique, change it and try again.");
        return new TagInvalidRequestException("Tag with ( name =  " + newTag.getName()
                + ") already exist. This field must be unique, change it and try again.");
    }
}

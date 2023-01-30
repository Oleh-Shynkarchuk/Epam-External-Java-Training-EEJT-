package com.epam.esm.tag.service;

import com.epam.esm.errorhandle.constants.ErrorConstants;
import com.epam.esm.errorhandle.validation.Validator;
import com.epam.esm.tag.entity.Tag;
import com.epam.esm.tag.exception.TagNotFoundException;
import com.epam.esm.tag.repo.TagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final Validator validator;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, Validator validator) {
        this.tagRepository = tagRepository;
        this.validator = validator;
    }

    @Override
    public List<Tag> getAllTag(Pageable paginationCriteria) {
        log.debug("Start of getAllTag method in service layer." +
                "For valid non erroneous pageable request get amount of all tags in repository");
        long count = tagRepository.count();

        log.debug("Validate pagination request.");
        Pageable pageable = validator.validPageableRequest(count, paginationCriteria);

        log.debug("Get tags from repository with pagination");
        Page<Tag> allTags = tagRepository.findAll(pageable);

        log.debug("Emptiness check.");
        if (allTags.isEmpty()) {
            throw tagNotFoundException();
        }
        log.debug("Service return received tags from repository");
        return allTags.toList();
    }

    @Override
    public Tag getTag(Long id) {
        log.debug("Start of getTagById method in service layer. " +
                "Get tag by id from repository");
        Tag tag = tagRepository.findById(id).orElseThrow(this::tagNotFoundException);

        log.debug("Service return received tag from repository");
        return tag;
    }

    @Override
    public boolean existByName(String name) {
        log.debug("Start of existByName method in service layer. " +
                "Return check result is exist tag by name in repository");
        return tagRepository.existsByName(name);
    }

    public Tag getTagByName(String tagName) {
        log.debug("Start of existByName method in service layer. " +
                "Get tag by name in repository");
        Tag byName = tagRepository.getByName(tagName);

        log.debug("Service return received tag from repository");
        return byName;
    }

    @Override
    public Tag createTag(Tag newTag) {
        log.debug("Start of create new tag method in service layer." +
                "Validate new tag fields");
        validator.tag(newTag);

        log.debug("Save new tag in repository");
        Tag tag = tagRepository.saveAndFlush(newTag);

        log.debug("Service return received new tag from repository");
        return tag;
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
        Tag tag = getTag(id);

        log.debug("Service return received new MostWidelyUsedTag from repository");
        return tag;
    }

    private TagNotFoundException tagNotFoundException() {
        log.error(ErrorConstants.TAG_NOT_FOUND_MESSAGE);
        return new TagNotFoundException();
    }
}

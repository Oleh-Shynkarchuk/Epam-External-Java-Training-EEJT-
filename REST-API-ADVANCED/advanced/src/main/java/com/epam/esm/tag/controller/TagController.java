package com.epam.esm.tag.controller;

import com.epam.esm.errorhandle.validation.Validator;
import com.epam.esm.hateoas.HateoasSupport;
import com.epam.esm.tag.entity.Tag;
import com.epam.esm.tag.exception.TagInvalidRequestException;
import com.epam.esm.tag.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/api/tag")
public class TagController {

    private final TagService tagService;
    private final HateoasSupport hateoasSupport;
    private final Validator validator;

    @Autowired
    public TagController(TagService tagService, HateoasSupport hateoasSupport, Validator validator) {
        this.tagService = tagService;
        this.hateoasSupport = hateoasSupport;
        this.validator = validator;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<Tag>> getAllTag(@ParameterObject Pageable paginationCriteria) {

        log.debug("Request accepted getAllTag. Send request to service.");
        List<Tag> allTag = tagService.getAllTag(paginationCriteria);

        log.debug("Add hateoas to tags");
        CollectionModel<Tag> tagsAndHateoas = hateoasSupport.addHateoasSupportToAllTag(allTag, paginationCriteria);

        log.debug("Send response to client");
        return ResponseEntity.ok(tagsAndHateoas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> getTagById(@PathVariable String id) {

        log.debug("Request accepted getTagById. Validate id field.");
        if (validator.isPositiveAndParsableId(id)) {

            log.debug("Send request to service");
            Tag serviceTag = tagService.getTag(Long.parseLong(id));

            log.debug("Add hateoas to tag");
            Tag tagAndHateoas = hateoasSupport.addHateoasSupportToSingleTag(serviceTag);

            log.debug("Send response to client");
            return ResponseEntity.ok(tagAndHateoas);
        } else {
            throw getTagInvalidRequestException(id);
        }
    }

    @GetMapping("/best")
    public ResponseEntity<Tag> getMostWidelyUsedTag() {

        log.debug("Request accepted getMostWidelyUsedTag. Send request to service.");
        Tag mostWidelyUsedTag = tagService.getMostWidelyUsedTag();

        log.debug("Add hateoas to tag");
        Tag tagAndHateoas = hateoasSupport.addHateoasSupportToSingleTag(mostWidelyUsedTag);

        log.debug("Send response to client");
        return ResponseEntity.ok(tagAndHateoas);
    }

    @PostMapping
    public ResponseEntity<Tag> createTag(@RequestBody Tag newTag) {

        log.debug("Request accepted getMostWidelyUsedTag. Send request to service.");
        Tag serviceTag = tagService.createTag(newTag);

        log.debug("Add hateoas to tag");
        Tag tagAndHateoas = hateoasSupport.addHateoasSupportToSingleTag(serviceTag);

        log.debug("Send response to client");
        return ResponseEntity.ok(tagAndHateoas);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTag(@PathVariable String id) {

        log.debug("Request accepted deleteTag. Validate id field.");
        if (validator.isPositiveAndParsableId(id)) {

            log.debug("Send request to service");
            tagService.deleteTag(Long.valueOf(id));

            log.debug("Send response to client");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw getTagInvalidRequestException(id);
        }
    }

    private TagInvalidRequestException getTagInvalidRequestException(String id) {
        log.error("Invalid input ( id = " + id
                + " ). Only a positive number is allowed ( 1 and more ).");
        return new TagInvalidRequestException("Invalid input ( id = " + id
                + " ). Only a positive number is allowed ( 1 and more ).");
    }
}

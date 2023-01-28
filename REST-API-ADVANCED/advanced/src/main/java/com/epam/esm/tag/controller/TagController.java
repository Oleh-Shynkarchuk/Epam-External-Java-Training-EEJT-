package com.epam.esm.tag.controller;

import com.epam.esm.errorhandle.validation.Validate;
import com.epam.esm.hateoas.HateoasSupport;
import com.epam.esm.tag.entity.Tag;
import com.epam.esm.tag.exception.TagInvalidRequestException;
import com.epam.esm.tag.service.TagService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/api/tag")
public class TagController {

    private final TagService tagService;
    private final HateoasSupport hateoasSupport;
    private final Validate validate;

    @Autowired
    public TagController(TagService tagService, HateoasSupport hateoasSupport, Validate validate) {
        this.tagService = tagService;
        this.hateoasSupport = hateoasSupport;
        this.validate = validate;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<Tag>> getAllTag(@ParameterObject Pageable paginationCriteria) {
        return ResponseEntity.ok(
                hateoasSupport.addHateoasSupportToAllTag(
                        tagService.getAllTag(paginationCriteria),
                        paginationCriteria
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> getTagById(@PathVariable String id) {
        if (validate.id(id)) {
            return ResponseEntity.ok(
                    hateoasSupport.addHateoasSupportToSingleTag(
                            tagService.getTag(Long.parseLong(id))));
        } else throw new TagInvalidRequestException("Invalid input ( id = " + id
                + " ). Only a positive number is allowed ( 1 and more ).");
    }

    @GetMapping("/best")
    public ResponseEntity<Tag> getMostWidelyUsedTag() {
        return ResponseEntity.ok(
                hateoasSupport.addHateoasSupportToSingleTag(tagService.getMostWidelyUsedTag())
        );
    }

    @PostMapping
    public ResponseEntity<Tag> createTag(@RequestBody Tag newTag) {
        return ResponseEntity.ok(
                hateoasSupport.addHateoasSupportToSingleTag(tagService.createTag(newTag))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTag(@PathVariable String id) {
        if (validate.id(id)) {
            tagService.deleteTag(Long.valueOf(id));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else throw new TagInvalidRequestException("Invalid input ( id = " + id
                + " ). Only a positive number is allowed ( 1 and more ).");
    }
}

package com.epam.esm.tag.controller;

import com.epam.esm.tag.entity.Tag;
import com.epam.esm.tag.exception.TagInvalidRequestException;
import com.epam.esm.tag.hateoas.TagHateoasSupport;
import com.epam.esm.tag.service.TagService;
import com.epam.esm.tag.validation.TagValidator;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TagController {

    private final TagService tagService;
    private final TagHateoasSupport hateoasSupport;
    private final TagValidator validator;

    @GetMapping
    public ResponseEntity<CollectionModel<Tag>> getAllTags(@ParameterObject Pageable paginationCriteria) {

        log.debug("Request accepted getCategoryFromCache. " +
                "Pagination object request." + paginationCriteria);
        List<Tag> allTag = tagService.getAllTags(paginationCriteria);
        CollectionModel<Tag> tagsAndHateoas = hateoasSupport.addHateoasSupportToAllTag(allTag, paginationCriteria);
        log.debug("Send response tags list : " + tagsAndHateoas.toString() + " to client");
        return ResponseEntity.ok(tagsAndHateoas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> getTagById(@PathVariable String id) {

        log.debug("Request accepted getTagById. " +
                "Id param request = " + id);
        String idResponse = validator.isPositiveAndParsableIdResponse(id);
        if (idResponse.isEmpty()) {
            Tag serviceTag = tagService.getTagById(id);
            Tag tagAndHateoas = hateoasSupport.addHateoasSupportToSingleTag(serviceTag);
            log.debug("Send response tag: " + tagAndHateoas.toString() + " to client");
            return ResponseEntity.ok(tagAndHateoas);
        }
        throw tagInvalidRequestException(idResponse);
    }

    @GetMapping("/best")
    public ResponseEntity<Tag> getMostWidelyUsedTag() {
        log.debug("Request accepted getMostWidelyUsedTag. Send request to service.");
        Tag mostWidelyUsedTag = tagService.getMostWidelyUsedTag();
        Tag tagAndHateoas = hateoasSupport.addHateoasSupportToSingleTag(mostWidelyUsedTag);
        log.debug("Send response tag: " + tagAndHateoas.toString() + " to client");
        return ResponseEntity.ok(tagAndHateoas);
    }

    @PostMapping
    public ResponseEntity<Tag> createTag(@RequestBody Tag newTag) {
        log.debug("Request accepted create new Tag. " +
                "new Tag object request : " + newTag.toString());
        String result = validator.isCreatableTagFieldsErrorResponse(newTag);
        if (result.isEmpty()) {
            Tag serviceTag = tagService.createTag(newTag);
            Tag tagAndHateoas = hateoasSupport.addHateoasSupportToSingleTag(serviceTag);
            log.debug("Send response tag: " + tagAndHateoas.toString() + " to client");
            return ResponseEntity.ok(tagAndHateoas);
        }
        throw tagInvalidRequestException(result);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTagById(@PathVariable String id) {

        log.debug("Request accepted deleteTagById." +
                "Id param request = " + id);
        String idResponse = validator.isPositiveAndParsableIdResponse(id);
        if (idResponse.isEmpty()) {
            tagService.deleteTagById(id);
            log.debug("Successful deleted tag");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        throw tagInvalidRequestException(idResponse);
    }

    private TagInvalidRequestException tagInvalidRequestException(String message) {
        log.error(message);
        return new TagInvalidRequestException(message);
    }
}

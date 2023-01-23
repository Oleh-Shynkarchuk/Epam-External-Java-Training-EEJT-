package com.epam.esm.tag.controller;

import com.epam.esm.Validate;
import com.epam.esm.certificate.controller.PaginationCriteria;
import com.epam.esm.tag.entity.Tag;
import com.epam.esm.tag.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/tag")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public List<Tag> getAllTag(PaginationCriteria paginationCriteria) {
        Validate.pagination(paginationCriteria);
        return tagService.getAllTag(paginationCriteria.getPage(), paginationCriteria.getSize());
    }

    @GetMapping("/{id}")
    public Tag getTagById(@PathVariable Long id) {
        Tag tag = tagService.getTag(id);
        AddHateoasSupportToTag(tag);
        return tag;
    }

    @GetMapping("/best")
    public ResponseEntity<Tag> getMostWidelyUsedTag() {
        return ResponseEntity.ok(
                AddHateoasSupportToTag(
                        tagService.getMostWidelyUsedTag()
                )
        );
    }

    @PostMapping
    public Tag createTag(@RequestBody Tag newTag) {
        return tagService.createTag(newTag);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public Tag AddHateoasSupportToTag(Tag t) {
        t.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class).getTagById(t.getId())).withRel("getTag"));
        t.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class).deleteTag(t.getId())).withRel("deleteTag"));
        return t;
    }
}

package com.epam.esm.tags.controller;


import com.epam.esm.tags.entity.Tag;
import com.epam.esm.tags.service.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
public class TagsController {
    private final TagsService tagsService;

    @Autowired
    public TagsController(TagsService tagsService) {
        this.tagsService = tagsService;
    }

    @GetMapping()
    public ResponseEntity<List<Tag>> getAllTags() {
        return new ResponseEntity<>(tagsService.read(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneTag(@PathVariable("id") Long id) {
        return new ResponseEntity<>(tagsService.read(id), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNewTag(@RequestBody Tag tag) {
        return new ResponseEntity<>(tagsService.create(tag), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteTagsById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(tagsService.delete(id), HttpStatus.NO_CONTENT);
    }
}

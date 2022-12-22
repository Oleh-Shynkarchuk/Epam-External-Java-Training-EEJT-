package com.epam.esm.tags.controller;


import com.epam.esm.tags.entity.Tag;
import com.epam.esm.tags.service.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
public class TagsController {
    @Autowired
    TagsService tagsService;

    @GetMapping()
    public ResponseEntity<List<Tag>> getAllTags() {
        return new ResponseEntity<>(tagsService.read(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneTag(@PathVariable("id") Long id) {
        if (id > 0) {
            Optional<Tag> read = tagsService.read(id);
            if (read.isPresent()) {
                return new ResponseEntity<>(read.get(), HttpStatus.OK);
            }
            return new ResponseEntity<>(List.of(Map.of("errorMessage", "Requested tag not found (id = " + id + ")"),
                    Map.of("errorCode", HttpStatus.NOT_FOUND)), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(List.of(Map.of("errorMessage", "Bad request (id = " + id + ") id can be from 1 up to ∞"),
                Map.of("errorCode", HttpStatus.BAD_REQUEST)), HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/new", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNewTag(@RequestBody Tag tag) {
        boolean isCreated = tagsService.create(tag);
        if (isCreated) {
            Optional<Tag> read = tagsService.read(tag.getName());
            if (read.isPresent()) {
                return new ResponseEntity<>(read.get(), HttpStatus.CREATED);
            }
        } else {
            Optional<Tag> read = tagsService.read(tag.getName());
            if (read.isPresent()) {
                return new ResponseEntity<>(List.of(Map.of("errorMessage", "Bad request (" + read.get() + ") already exist or something went wrong"),
                        Map.of("errorCode", HttpStatus.BAD_REQUEST)), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(List.of(Map.of("errorMessage", "An error occurrence while creating new tag"),
                Map.of("errorCode", HttpStatus.INTERNAL_SERVER_ERROR)), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<?> deleteTagsById(@PathVariable("id") Long id) {
        if (id > 0L) {
            Optional<Tag> tagIsExist = tagsService.read(id);
            if (tagIsExist.isPresent()) {
                boolean tagIsDeleted = tagsService.delete(id);
                if (tagIsDeleted) {
                    return new ResponseEntity<>(tagIsExist.get(), HttpStatus.OK);
                } else
                    return new ResponseEntity<>(List.of(Map.of("errorMessage", "Something went wrong while deleting tag"),
                            Map.of("errorCode", HttpStatus.INTERNAL_SERVER_ERROR)), HttpStatus.INTERNAL_SERVER_ERROR);
            } else
                return new ResponseEntity<>(List.of(Map.of("errorMessage", "Requested tag not found (id = " + id + ")"),
                        Map.of("errorCode", HttpStatus.BAD_REQUEST)), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(List.of(Map.of("errorMessage", "Bad request (id = " + id + ") id can be from 1 up to ∞"),
                Map.of("errorCode", HttpStatus.BAD_REQUEST)), HttpStatus.BAD_REQUEST);
    }
}

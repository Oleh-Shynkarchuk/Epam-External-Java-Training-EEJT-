package com.epam.esm.giftcertificates.controller;

import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.service.GiftCertificatesService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/gifts", produces = MediaType.APPLICATION_JSON_VALUE)
public class GiftsController {
    @Autowired
    GiftCertificatesService giftCertificatesService;

    @GetMapping()
    public ResponseEntity<List<GiftCertificate>> getAllTags() {
        return new ResponseEntity<>(giftCertificatesService.read(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneGiftCertificate(@PathVariable("id") Long id) {
        if (id > 0) {
            Optional<GiftCertificate> read = giftCertificatesService.read(id);
            if (read.isPresent()) {
                return new ResponseEntity<>(read.get(), HttpStatus.OK);
//                return new ResponseEntity<>(new GiftCertificate(1L,"Gift","",new BigDecimal("245.12"), Duration.ZERO,List.of(new Tag(1L,"Discount"),new Tag(2L,"free"))), HttpStatus.OK);
            }
            return new ResponseEntity<>(List.of(Map.of("errorMessage", "Requested tag not found (id = " + id + ")"),
                    Map.of("errorCode", HttpStatus.NOT_FOUND)), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(List.of(Map.of("errorMessage", "Bad request (id = " + id + ") id can be from 1 up to ∞"),
                Map.of("errorCode", HttpStatus.BAD_REQUEST)), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getOneGiftCertificate(
            @RequestParam(required = false) String tagName,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String sortByDate,
            @RequestParam(required = false) String sortByName) {
        boolean validation = dataValidation(tagName, name, description, sortByName, sortByName);
        if (validation) {
            String sqlQuery = "SELECT * FROM giftcertificate gc" +
                    " INNER JOIN giftcertificate_has_tag ght" +
                    " ON ght.giftcertificate_id = gc.id" +
                    " INNER JOIN tags t" +
                    " ON t.id = ght.tag_id" +
                    " WHERE" + (tagName != null ?
                    " t.name = '" + tagName + "' AND" : "");
            sqlQuery = sqlQuery + (name != null ? " gc.name LIKE '%" + name + "%'" + (description != null ? " AND gc.description LIKE '%" + description + "%'" : "") : (description != null ? " gc.description LIKE '%" + description + "%'" : ""));
            sqlQuery = sqlQuery + (sortByDate != null ? " ORDER BY gc.create_date " + sortByDate + "," + (sortByName != null ? "gc.name " + sortByName : "") : (sortByName != null ? " ORDER BY gc.name " + sortByName : ""));
            System.out.println("sqlQuery = " + sqlQuery);
            Optional<GiftCertificate> read = giftCertificatesService.read(sqlQuery);
            return null;
        } else return null;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNewTag(@RequestBody GiftCertificate giftCertificate) {
        System.out.println("gft = " +giftCertificate);
        boolean isCreated = giftCertificatesService.create(giftCertificate);
        Optional<GiftCertificate> read = giftCertificatesService.read(giftCertificate.getName());
        if (isCreated) {
            if (read.isPresent()) {
                return new ResponseEntity<>(read.get(), HttpStatus.CREATED);
            }
        } else {
            if (read.isPresent()) {
                return new ResponseEntity<>(List.of(Map.of("errorMessage", "Bad request (" + read.get() + ") already exist or something went wrong"),
                        Map.of("errorCode", HttpStatus.BAD_REQUEST)), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(List.of(Map.of("errorMessage", "An error occurrence while creating new tag"),
                Map.of("errorCode", HttpStatus.INTERNAL_SERVER_ERROR)), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PatchMapping(value = "/{id}/patch", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> patchNewTag(@PathVariable("id") Long id, @RequestBody GiftCertificate giftCertificate) {
        Optional<GiftCertificate> read = giftCertificatesService.read(id);
        if (read.isPresent()) {
            boolean isCreated = giftCertificatesService.update(id, giftCertificate);
            if (isCreated) {
                    return new ResponseEntity<>(read.get(), HttpStatus.CREATED);
            }
            return new ResponseEntity<>(List.of(Map.of("errorMessage", "Bad request (" + read.get() + ") already exist or something went wrong"),
                    Map.of("errorCode", HttpStatus.BAD_REQUEST)), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(List.of(Map.of("errorMessage", "An error occurrence while creating new tag"),
                Map.of("errorCode", HttpStatus.INTERNAL_SERVER_ERROR)), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<?> deleteTagsById(@PathVariable("id") Long id) {
        if (id > 0L) {
            Optional<GiftCertificate> tagIsExist = giftCertificatesService.read(id);
            if (tagIsExist.isPresent()) {
                boolean tagIsDeleted = giftCertificatesService.delete(id);
                if (tagIsDeleted) {
                    return new ResponseEntity<>(tagIsExist.get(), HttpStatus.OK);
                } else
                    return new ResponseEntity<>(List.of(Map.of("errorMessage", "An error occurred while deleting the certificate"),
                            Map.of("errorCode", HttpStatus.INTERNAL_SERVER_ERROR)), HttpStatus.INTERNAL_SERVER_ERROR);
            } else
                return new ResponseEntity<>(List.of(Map.of("errorMessage", "Requested the certificate was not found (id = " + id + ")"),
                        Map.of("errorCode", HttpStatus.NOT_FOUND)), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(List.of(Map.of("errorMessage", "Bad request (id = " + id + ") id can`t be negative or 0"),
                Map.of("errorCode", HttpStatus.BAD_REQUEST)), HttpStatus.BAD_REQUEST);
    }

    private boolean dataValidation(String tagName, String name, String description, String sortByName, String sortByName1) {
        return true;
    }
}

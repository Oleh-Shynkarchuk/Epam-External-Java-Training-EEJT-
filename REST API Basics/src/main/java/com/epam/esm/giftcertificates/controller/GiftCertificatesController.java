package com.epam.esm.giftcertificates.controller;


import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.filter.entity.SearchParams;
import com.epam.esm.giftcertificates.filter.entity.SearchParamsBuilder;
import com.epam.esm.giftcertificates.service.GiftCertificatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/gifts", produces = MediaType.APPLICATION_JSON_VALUE)
public class GiftCertificatesController {
    private final GiftCertificatesService giftCertificatesService;
    private final SearchParamsBuilder searchParamsBuilder;

    @Autowired
    public GiftCertificatesController(GiftCertificatesService giftCertificatesService, SearchParamsBuilder searchParamsBuilder) {
        this.giftCertificatesService = giftCertificatesService;
        this.searchParamsBuilder = searchParamsBuilder;
    }

    @GetMapping
    public ResponseEntity<List<GiftCertificate>> getAllGiftCertificates() {
        return new ResponseEntity<>(giftCertificatesService.readAllGiftCertificates(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificate> getGiftCertificateById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(giftCertificatesService.readGiftCertificate(id), HttpStatus.OK);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<List<GiftCertificate>> searchGiftCertificates(
            @RequestParam(required = false) String tagName,
            @RequestParam(required = false) String giftName,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String sortDate,
            @RequestParam(required = false) String sortName) {
        SearchParams searchParams = searchParamsBuilder.setTagName(tagName).setGiftName(giftName)
                .setDescription(description).setSortDate(sortDate).setSortName(sortName).create();
        return new ResponseEntity<>(giftCertificatesService.readGiftCertificate(searchParams), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GiftCertificate> createNewGiftCertificate(@RequestBody GiftCertificate giftCertificate) {
        return new ResponseEntity<>(giftCertificatesService.createGiftCertificate(giftCertificate), HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GiftCertificate> patchGiftCertificate(@PathVariable("id") Long id, @RequestBody GiftCertificate giftCertificate) {
        return new ResponseEntity<>(giftCertificatesService.updateGiftCertificate(id, giftCertificate), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteGiftCertificateById(@PathVariable("id") Long id) {
        giftCertificatesService.deleteGiftCertificate(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
package com.epam.esm.giftcertificates.controller;


import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.service.GiftCertificatesService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
@RestController
@RequestMapping(value = "/gifts", produces = MediaType.APPLICATION_JSON_VALUE)
public class GiftsController {

    private final GiftCertificatesService giftCertificatesService;
    @Autowired
    public GiftsController(GiftCertificatesService giftCertificatesService) {
        this.giftCertificatesService = giftCertificatesService;
    }

    @GetMapping()
    public ResponseEntity<List<GiftCertificate>> getAllTags() {
        return new ResponseEntity<>(giftCertificatesService.read(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificate> getOneGiftCertificate(@PathVariable("id") Long id) {
        return new ResponseEntity<>(giftCertificatesService.read(id), HttpStatus.OK);
    }

    @GetMapping(value = "/")
    public ResponseEntity<List<GiftCertificate>> getOneGiftCertificate(
            @RequestParam(required = false) String tagName,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String sortByDate,
            @RequestParam(required = false) String sortByName) {
        return new ResponseEntity<>(giftCertificatesService.read(tagName,name,description,sortByDate,sortByName), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GiftCertificate> createNewTag(@RequestBody GiftCertificate giftCertificate) {
        return new ResponseEntity<>(giftCertificatesService.create(giftCertificate), HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GiftCertificate> patchNewTag(@PathVariable("id") Long id, @RequestBody GiftCertificate giftCertificate) {
        return new ResponseEntity<>(giftCertificatesService.update(id, giftCertificate), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteTagsById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(giftCertificatesService.delete(id), HttpStatus.NO_CONTENT);
    }
}

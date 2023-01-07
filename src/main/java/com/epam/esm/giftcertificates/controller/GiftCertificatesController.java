package com.epam.esm.giftcertificates.controller;


import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.service.GiftCertificatesService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/gifts", produces = MediaType.APPLICATION_JSON_VALUE)
public class GiftCertificatesController {

    private final GiftCertificatesService giftCertificatesService;

    @Autowired
    public GiftCertificatesController(GiftCertificatesService giftCertificatesService) {
        this.giftCertificatesService = giftCertificatesService;
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
    public ResponseEntity<List<GiftCertificate>> searchGiftCertificates(@RequestParam(required = false) String tag_name,
                                                                        @RequestParam(required = false) String gift_name,
                                                                        @RequestParam(required = false) String description,
                                                                        @RequestParam(required = false) String sort_date,
                                                                        @RequestParam(required = false) String sort_name) {
        Map<String, String> tagName = new HashMap<>();
        tagName.put("tag_name", tag_name);
        tagName.put("gift_name", gift_name);
        tagName.put("description", description);
        tagName.put("sort_date", sort_date);
        tagName.put("sort_name", sort_name);
        return new ResponseEntity<>(giftCertificatesService.readGiftCertificate(tagName), HttpStatus.OK);
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

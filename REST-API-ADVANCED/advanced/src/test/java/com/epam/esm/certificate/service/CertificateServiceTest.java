package com.epam.esm.certificate.service;

import com.epam.esm.certificate.entity.Certificate;
import com.epam.esm.certificate.exception.CertificateInvalidRequestException;
import com.epam.esm.certificate.exception.CertificateNotFoundException;
import com.epam.esm.certificate.repo.CertificateRepository;
import com.epam.esm.certificate.validation.CertificateValidator;
import com.epam.esm.tag.entity.Tag;
import com.epam.esm.tag.service.TagServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class CertificateServiceTest {

    @Mock
    private CertificateRepository certificateRepository;
    @Mock
    private CertificateValidator validator;
    @Mock
    private TagServiceImpl tagService;
    @InjectMocks
    private CertificateServiceImpl certificateService;

    @Test
    void getAllCertificateReturnListOfCertificates() {
        Certificate expected1 = Certificate.builder().id(1L).name("TestCertificate1")
                .description("TestDescription1").price(BigDecimal.valueOf(100))
                .durationOfDays("15").tags(List.of(
                        Tag.builder().id(1L).name("TestTag1").build(),
                        Tag.builder().id(2L).name("TestTag2").build())
                ).build();
        Certificate expected2 = Certificate.builder().id(2L).name("TestCertificate2")
                .description("TestDescription2").price(BigDecimal.valueOf(300))
                .durationOfDays("35").tags(
                        List.of(
                                Tag.builder().id(1L).name("TestTag1").build(),
                                Tag.builder().id(3L).name("TestTag3").build())
                ).build();
        long availableAmount = 11L;
        Pageable pageable = PageRequest.of(0, 10);
        Page<Certificate> expectedList = new PageImpl<>(List.of(expected1, expected2));

        Mockito.when(certificateRepository.count()).thenReturn(availableAmount);
        Mockito.when(validator.validPageableRequest(availableAmount, pageable)).thenReturn(pageable);
        Mockito.when(certificateRepository.findAll(pageable)).thenReturn(expectedList);

        assertEquals(expectedList.toList(), certificateService.getAllCertificates(pageable));
    }

    @Test
    void getAllCertificateShouldThrowItemNotFound() {
        long availableAmount = 0L;
        Pageable pageable = PageRequest.of(0, 10);
        Page<Certificate> expectedList = new PageImpl<>(List.of());
        Mockito.when(certificateRepository.count()).thenReturn(availableAmount);
        Mockito.when(validator.validPageableRequest(availableAmount, pageable)).thenReturn(pageable);
        Mockito.when(certificateRepository.findAll(pageable)).thenReturn(expectedList);

        assertThrows(CertificateNotFoundException.class, () -> certificateService.getAllCertificates(pageable));
    }

    @Test
    void getCertificateByIdShouldReturnCertificate() {
        long id = 1L;
        Certificate expected1 = Certificate.builder().id(id).name("TestCertificate1")
                .description("TestDescription1").price(BigDecimal.valueOf(100))
                .durationOfDays("15").tags(
                        List.of(
                                Tag.builder().id(1L).name("TestTag1").build(),
                                Tag.builder().id(2L).name("TestTag2").build())
                ).build();
        Mockito.when(certificateRepository.findById(id)).thenReturn(Optional.of(expected1));

        assertEquals(expected1, certificateService.getCertificateById(id));
    }

    @Test
    void getCertificateByIdShouldThrowItemNotFound() {
        long id = 1L;

        Mockito.when(certificateRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CertificateNotFoundException.class, () -> certificateService.getCertificateById(id));
    }

    @Test
    void deleteCertificateByIdVerify() {
        long id = 1L;
        Mockito.when(certificateRepository.existsById(id)).thenReturn(true);
        Mockito.doNothing().when(certificateRepository).deleteById(id);
        certificateService.deleteCertificateById(id);
        Mockito.verify(certificateRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    void deleteCertificateByIdShouldThrowItemNotFound() {
        long id = 1L;

        Mockito.when(certificateRepository.existsById(id)).thenReturn(false);

        assertThrows(CertificateNotFoundException.class, () -> certificateService.deleteCertificateById(id));
    }

    @Test
    void createCertificateReturnNewCertificate() {
        Tag firstTag = Tag.builder().id(1L).name("TestTag1").build();
        Tag secondTag = Tag.builder().id(2L).name("TestTag2").build();
        List<Tag> tags = List.of(firstTag, secondTag);
        long id = 1L;
        Certificate newCertificate = Certificate.builder().id(null).name("TestCertificate1")
                .description("TestDescription1").price(BigDecimal.valueOf(100))
                .durationOfDays("15").tags(tags).build();
        Certificate expected = Certificate.builder().id(id).name("TestCertificate1")
                .description("TestDescription1").price(BigDecimal.valueOf(100))
                .durationOfDays("15").tags(tags).build();

        Mockito.when(certificateRepository.existsByName(newCertificate.getName())).thenReturn(false);
        Mockito.when(certificateRepository.saveAndFlush(newCertificate)).thenReturn(expected);
        Mockito.when(tagService.getTagByName(firstTag.getName())).thenReturn(Optional.of(firstTag));

        assertEquals(expected, certificateService.createCertificate(newCertificate));
    }

    @Test
    void createCertificateShouldTrowInvalidRequestWhenCertificateNameExist() {

        Certificate newCertificate = Certificate.builder().id(null).name("TestCertificate1")
                .description("TestDescription1").price(BigDecimal.valueOf(100))
                .durationOfDays("15").tags(
                        List.of(
                                Tag.builder().id(1L).name("TestTag1").build(),
                                Tag.builder().id(2L).name("TestTag2").build())
                ).build();

        Mockito.when(certificateRepository.existsByName(newCertificate.getName())).thenReturn(true);

        assertThrows(CertificateInvalidRequestException.class, () -> certificateService.createCertificate(newCertificate));
    }

    @Test
    void patchCertificateReturnUpdatedCertificate() {
        Tag firstTag = Tag.builder().id(1L).name("TestTag1").build();
        Tag secondTag = Tag.builder().id(2L).name("TestTag2").build();
        List<Tag> tags = List.of(firstTag, secondTag);
        long id = 10L;
        Certificate newCertificate = Certificate.builder().id(null).name("TestCertificate1")
                .description("UPDATED DESCRIPTION").price(BigDecimal.valueOf(200))
                .tags(null).build();
        Certificate previousCertificate = Certificate.builder().id(id).name("TestCertificate1")
                .description("TestDescription1").price(BigDecimal.valueOf(300))
                .durationOfDays("25").tags(tags).build();
        Certificate expectedCertificate = Certificate.builder().id(id).name("TestCertificate1")
                .description("UPDATED DESCRIPTION").price(BigDecimal.valueOf(200))
                .durationOfDays("25").tags(tags).build();


        Mockito.when(certificateRepository.findById(id)).thenReturn(Optional.of(previousCertificate));
        Mockito.when(certificateRepository.existsByName(newCertificate.getName())).thenReturn(true);
        Mockito.when(certificateRepository.findByName(newCertificate.getName())).thenReturn(previousCertificate);
        Mockito.when(tagService.getTagByName(firstTag.getName())).thenReturn(Optional.of(firstTag));
        Mockito.when(tagService.getTagByName(secondTag.getName())).thenReturn(Optional.of(secondTag));
        Mockito.when(certificateRepository.saveAndFlush(expectedCertificate)).thenReturn(expectedCertificate);

        assertEquals(expectedCertificate, certificateService.patchCertificate(id, newCertificate));
    }

    @Test
    void patchCertificateShouldThrowItemNotFound() {
        long id = 10L;
        Certificate newCertificate = Certificate.builder().id(null).name("TestCertificate1")
                .description("UPDATED DESCRIPTION").price(BigDecimal.valueOf(200))
                .tags(null).build();


        Mockito.when(certificateRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CertificateNotFoundException.class, () -> certificateService.patchCertificate(id, newCertificate));
    }

    @Test
    void patchCertificateShouldTrowInvalidRequestWhenInvalidFieldsValue() {
        Tag firstTag = Tag.builder().id(1L).name("TestTag1").build();
        Tag secondTag = Tag.builder().id(2L).name("TestTag2").build();
        List<Tag> tags = List.of(firstTag, secondTag);
        long id = 7L;
        long anotherIdForCertificateWithIdenticalName = 25L;
        Certificate newCertificate = Certificate.builder().id(null).name("TestCertificate1")
                .description("UPDATED DESCRIPTION").price(BigDecimal.valueOf(200))
                .tags(null).build();
        Certificate throwCertificate = Certificate.builder().id(anotherIdForCertificateWithIdenticalName)
                .name("TestCertificate1")
                .description("TestDescription1").price(BigDecimal.valueOf(300))
                .durationOfDays("25").tags(tags).build();

        Mockito.when(certificateRepository.existsByName(newCertificate.getName())).thenReturn(true);
        Mockito.when(certificateRepository.findByName(newCertificate.getName())).thenReturn(throwCertificate);

        assertThrows(CertificateInvalidRequestException.class, () -> certificateService.patchCertificate(id, newCertificate));
    }

    @Test
    void getCertificateByTagsName() {
        Tag firstTag = Tag.builder().id(1L).name("TestTag1").build();
        Tag secondTag = Tag.builder().id(2L).name("TestTag2").build();
        Tag thirdTag = Tag.builder().id(3L).name("TestTag3").build();
        List<String> searchRequest = List.of(secondTag.getName(), thirdTag.getName());
        Pageable pageable = PageRequest.of(0, 10);
        Certificate expected = Certificate.builder().id(7L).name("TestCertificate1")
                .description("Description").price(BigDecimal.valueOf(200))
                .tags(List.of(firstTag, secondTag, thirdTag)).build();

        Mockito.when(certificateRepository.findByTagsNameAndPagination(
                searchRequest, (long) searchRequest.size(), pageable)).thenReturn(new PageImpl<>(List.of(expected), pageable, 1));

        assertEquals(List.of(expected), certificateService.getCertificateByTagsName(pageable, searchRequest));
    }

    @Test
    void getCertificateByTagsNameShouldThrowItemNotFound() {
        Tag secondTag = Tag.builder().id(2L).name("TestTag2").build();
        Tag thirdTag = Tag.builder().id(3L).name("TestTag3").build();
        List<String> searchRequest = List.of(secondTag.getName(), thirdTag.getName());
        Pageable pageable = PageRequest.of(0, 10);

        Mockito.when(certificateRepository.findByTagsNameAndPagination(
                searchRequest, (long) searchRequest.size(), pageable)).thenReturn(Page.empty());

        assertThrows(CertificateNotFoundException.class, () ->
                certificateService.getCertificateByTagsName(pageable, searchRequest));
    }

    @Test
    void findAllById() {
        List<Long> idList = List.of(1L, 2L, 3L);
        Certificate expected1 = Certificate.builder().id(1L).name("TestCertificate1")
                .description("Description").price(BigDecimal.valueOf(200))
                .tags(List.of()).build();
        Certificate expected2 = Certificate.builder().id(2L).name("TestCertificate2")
                .description("Description").price(BigDecimal.valueOf(300))
                .tags(List.of()).build();
        Certificate expected3 = Certificate.builder().id(3L).name("TestCertificate3")
                .description("Description").price(BigDecimal.valueOf(400))
                .tags(List.of()).build();

        Mockito.when(certificateRepository.findAllById(idList))
                .thenReturn(List.of(expected1, expected2, expected3));

        assertEquals(List.of(expected1, expected2, expected3), certificateService.findAllById(idList));
    }

    @Test
    void findAllByIdShouldThrowItemNotFound() {
        List<Long> idList = List.of(1L, 2L, 3L);
        Certificate expected2 = Certificate.builder().id(2L).name("TestCertificate2")
                .description("Description").price(BigDecimal.valueOf(300))
                .tags(List.of()).build();
        Certificate expected3 = Certificate.builder().id(3L).name("TestCertificate3")
                .description("Description").price(BigDecimal.valueOf(400))
                .tags(List.of()).build();

        Mockito.when(certificateRepository.findAllById(idList))
                .thenReturn(List.of(expected2, expected3));

        assertThrows(CertificateNotFoundException.class, () -> certificateService.findAllById(idList));
    }
}
package com.epam.esm.giftcertificates.service;

import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.exception.CertificateInvalidRequestException;
import com.epam.esm.giftcertificates.exception.CertificateNotFoundException;
import com.epam.esm.giftcertificates.exception.CertificateNotRepresent;
import com.epam.esm.giftcertificates.filter.entity.SearchParamsBuilder;
import com.epam.esm.integration.sqlrepo.Constants;
import com.epam.esm.integration.sqlrepo.MySQLRepository;
import com.epam.esm.tags.entity.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class GiftCertificatesServiceTest {

    @Mock
    private MySQLRepository giftCertificatesRepository;

    @InjectMocks
    private GiftCertificatesServiceImpl giftCertificatesService;

    @Test
    public void readGiftCertificateByMapOfRequestParamShouldThrowInvalidRequest() {
        SearchParamsBuilder searchParamsBuilder = new SearchParamsBuilder();
        assertThrows(CertificateInvalidRequestException.class, () -> giftCertificatesService.readGiftCertificate
                (searchParamsBuilder.setTagName("").create()));

        assertThrows(CertificateInvalidRequestException.class, () -> giftCertificatesService.readGiftCertificate
                (searchParamsBuilder.setGiftName("        ").create()));

        assertThrows(CertificateInvalidRequestException.class, () -> giftCertificatesService.readGiftCertificate
                (searchParamsBuilder.setDescription(" ").create()));

        assertThrows(CertificateInvalidRequestException.class, () -> giftCertificatesService.readGiftCertificate
                (searchParamsBuilder.setSortDate("fASCf").create()));

        assertThrows(CertificateInvalidRequestException.class, () -> giftCertificatesService.readGiftCertificate(
                searchParamsBuilder.setSortName("fdescq").create()));
    }

    @Test
    public void readGiftCertificateByMapOfRequestParamShouldThrowItemNotFound() {
        SearchParamsBuilder searchParamsBuilder = new SearchParamsBuilder();
        Mockito.when(giftCertificatesRepository.getGiftCertificateByParam(
                Constants.BuildQuery.SEARCH_BASE_QUERY + Constants.BuildQuery.SEARCH_TAG_NAME_QUERY_PART,
                List.of("tag1"))).thenReturn(Optional.empty());

        assertThrows(CertificateNotFoundException.class, () -> giftCertificatesService.readGiftCertificate(searchParamsBuilder.setTagName("tag1").create()));
    }

    @Test
    public void readGiftCertificateByMapOfRequestParamShouldReturnCertificatesWithTags() {
        SearchParamsBuilder searchParamsBuilder = new SearchParamsBuilder();
        GiftCertificate expected = new GiftCertificate(4L, "testCertfName", "testDescription",
                BigDecimal.valueOf(125.25), "15", LocalDateTime.now().toString(), null, List.of(new Tag(2L, "testTag1"), new Tag(3L, "AnotherTagName")));

        Mockito.when(giftCertificatesRepository.getGiftCertificateByParam(Constants.BuildQuery.SEARCH_BASE_QUERY
                + Constants.BuildQuery.SEARCH_TAG_NAME_QUERY_PART + Constants.BuildQuery.AND_DESCRIPTION_LIKE, List.of("testTag1", "%script%"))).thenReturn(Optional.of(List.of(expected)));

        assertEquals(List.of(expected), giftCertificatesService.readGiftCertificate(searchParamsBuilder.setTagName("testTag1").setDescription("script").create()));
    }

    @Test
    public void readAllGiftCertificateShouldThrowItemNotFound() {

        Mockito.when(giftCertificatesRepository.getAllGiftCertificates()).thenReturn(Optional.empty());

        assertThrows(CertificateNotFoundException.class, () -> giftCertificatesService.readAllGiftCertificates());
    }

    @Test
    public void readAllGiftCertificateShouldReturnListOfCertificates() {
        List<GiftCertificate> expectedList = List.of(
                new GiftCertificate(1L, "testCertfName", "testDescription",
                        BigDecimal.valueOf(125.25), "15", LocalDateTime.now().toString(),
                        null, List.of(new Tag(1L, "testTag1"))),
                new GiftCertificate(2L, "testCertfName", "testDescription",
                        BigDecimal.valueOf(325.25), "25", LocalDateTime.now().toString(),
                        null, List.of(new Tag(1L, "testTag1"), new Tag(2L, "AnotherTagName"))));

        Mockito.when(giftCertificatesRepository.getAllGiftCertificates()).thenReturn(Optional.of(expectedList));

        assertEquals(expectedList, giftCertificatesService.readAllGiftCertificates());
    }

    @Test
    public void createGiftCertificateShouldThrowInvalidRequest() {

        GiftCertificate nameExist = new GiftCertificate(4L, "testCertfName", "testDescription",
                BigDecimal.valueOf(125.25), "15", LocalDateTime.now().toString(), null, List.of(new Tag(2L, "testTag1"), new Tag(3L, "AnotherTagName")));

        Mockito.when(giftCertificatesRepository.isGiftCertificateByNameExist(nameExist.getName())).thenReturn(true);

        assertThrows(CertificateInvalidRequestException.class, () -> giftCertificatesService.createGiftCertificate(nameExist));
    }

    @Test
    public void createGiftCertificateShouldThrowNotRepresent() {

        GiftCertificate newCertificate = new GiftCertificate(4L, "testCertfName", "testDescription",
                BigDecimal.valueOf(125.25), "15", LocalDateTime.now().toString(), null, List.of(new Tag(2L, "testTag1"), new Tag(3L, "AnotherTagName")));

        Mockito.when(giftCertificatesRepository.isGiftCertificateByNameExist(newCertificate.getName())).thenReturn(false);
        Mockito.when(giftCertificatesRepository.createNewGiftCertificate(newCertificate)).thenReturn(Optional.empty());

        assertThrows(CertificateNotRepresent.class, () -> giftCertificatesService.createGiftCertificate(newCertificate));
    }

    @Test
    public void createGiftCertificateShouldReturnNewCertificate() {

        GiftCertificate newCertificate = new GiftCertificate(4L, "testCertfName", "testDescription",
                BigDecimal.valueOf(125.25), "15", LocalDateTime.now().toString(), null, List.of(new Tag(2L, "testTag1"), new Tag(3L, "AnotherTagName")));

        Mockito.when(giftCertificatesRepository.isGiftCertificateByNameExist(newCertificate.getName())).thenReturn(false);
        Mockito.when(giftCertificatesRepository.createNewGiftCertificate(newCertificate)).thenReturn(Optional.of(newCertificate));

        assertEquals(newCertificate, giftCertificatesService.createGiftCertificate(newCertificate));
    }

    @Test
    public void deleteGiftCertificateShouldThrowInvalidRequestCuzNegativeOrZeroID() {

        assertThrows(CertificateInvalidRequestException.class, () -> giftCertificatesService.deleteGiftCertificate(0L));

        assertThrows(CertificateInvalidRequestException.class, () -> giftCertificatesService.deleteGiftCertificate(-10L));
    }

    @Test
    public void deleteGiftCertificateShouldThrowNotFound() {

        long deleteId = 1L;
        Mockito.when(giftCertificatesRepository.getGiftCertificateById(deleteId)).thenReturn(Optional.empty());

        assertThrows(CertificateNotFoundException.class, () -> giftCertificatesService.deleteGiftCertificate(deleteId));
    }

    @Test
    public void deleteGiftCertificateShouldThrowNotRepresent() {
        GiftCertificate deleteCertificate = new GiftCertificate(4L, "testCertfName", "testDescription",
                BigDecimal.valueOf(125.25), "15", LocalDateTime.now().toString(), null, List.of(new Tag(2L, "testTag1"), new Tag(3L, "AnotherTagName")));

        Mockito.when(giftCertificatesRepository.getGiftCertificateById(deleteCertificate.getId())).thenReturn(Optional.of(deleteCertificate));
        Mockito.when(giftCertificatesRepository.deleteGiftCertificateById(deleteCertificate.getId())).thenReturn(false);

        assertThrows(CertificateNotRepresent.class, () -> giftCertificatesService.deleteGiftCertificate(deleteCertificate.getId()));
    }

    @Test
    public void deleteGiftCertificateVerify() {

        GiftCertificate deleteCertificate = new GiftCertificate(4L, "testCertfName", "testDescription",
                BigDecimal.valueOf(125.25), "15", LocalDateTime.now().toString(), null, List.of(new Tag(2L, "testTag1"), new Tag(3L, "AnotherTagName")));

        Mockito.when(giftCertificatesRepository.getGiftCertificateById(deleteCertificate.getId())).thenReturn(Optional.of(deleteCertificate));
        Mockito.when(giftCertificatesRepository.deleteGiftCertificateById(deleteCertificate.getId())).thenReturn(true);

        giftCertificatesService.deleteGiftCertificate(deleteCertificate.getId());

        Mockito.verify(giftCertificatesRepository).deleteGiftCertificateById(deleteCertificate.getId());
    }

    @Test
    public void readGiftCertificateByIdShouldThrowInvalidRequestCuzNegativeOrZeroID() {
        assertThrows(CertificateInvalidRequestException.class, () -> giftCertificatesService.readGiftCertificate(0L));

        assertThrows(CertificateInvalidRequestException.class, () -> giftCertificatesService.readGiftCertificate(-4L));
    }

    @Test
    public void readGiftCertificateByIdShouldReturnItemNotFoundExceptionWhenCertificateDoesNotExist() {

        Mockito.when(giftCertificatesRepository.getGiftCertificateById(1L)).thenReturn(Optional.empty());

        assertThrows(CertificateNotFoundException.class, () -> giftCertificatesService.readGiftCertificate(1L));
    }

    @Test
    public void readGiftCertificateByIdShouldReturnCertificate() {
        GiftCertificate expected = new GiftCertificate(4L, "testCertfName", "testDescription",
                BigDecimal.valueOf(125.25), "15", LocalDateTime.now().toString(), null, List.of(new Tag(2L, "testTag1"), new Tag(3L, "AnotherTagName")));


        Mockito.when(giftCertificatesRepository.getGiftCertificateById(expected.getId())).thenReturn(Optional.of(expected));

        assertEquals(expected, giftCertificatesService.readGiftCertificate(expected.getId()));
    }

    @Test
    public void updateGiftCertificateShouldReturnInvalidRequestCuzNegativeOrZeroID() {
        long negativeId = -1L;
        long zeroId = -1L;
        GiftCertificate newCertificate = new GiftCertificate(null, "name", "description", BigDecimal.valueOf(241.45), "1", null, null, null);

        assertThrows(CertificateInvalidRequestException.class, () -> giftCertificatesService.updateGiftCertificate(negativeId, newCertificate));

        assertThrows(CertificateInvalidRequestException.class, () -> giftCertificatesService.updateGiftCertificate(zeroId, newCertificate));

    }

    @Test
    public void updateGiftCertificateShouldReturnInvalidRequestCuzNegativeOrZeroPriceOrNegativeDurationOrInconvertible() {
        long positiveId = 5L;
        GiftCertificate newCertificateNegativePrice = new GiftCertificate(null, "name", "description", BigDecimal.valueOf(-241.45), "15", null, null, null);

        GiftCertificate newCertificateNegativeDuration = new GiftCertificate(null, "name", "description", BigDecimal.valueOf(541.45), "-15", null, null, null);

        assertThrows(CertificateInvalidRequestException.class, () -> giftCertificatesService.updateGiftCertificate(positiveId, newCertificateNegativePrice));
        assertThrows(CertificateInvalidRequestException.class, () -> giftCertificatesService.updateGiftCertificate(positiveId, newCertificateNegativeDuration));

    }

    @Test
    public void updateGiftCertificateShouldReturnNotFound() {
        long positiveId = 5L;
        GiftCertificate newCertificate = new GiftCertificate(null, "name", "description", BigDecimal.valueOf(241.45), "1", null, null, null);

        Mockito.when(giftCertificatesRepository.getGiftCertificateById(positiveId)).thenReturn(Optional.empty());

        assertThrows(CertificateNotFoundException.class, () -> giftCertificatesService.updateGiftCertificate(positiveId, newCertificate));
    }

    @Test
    public void updateGiftCertificateShouldReturnItemNotRepresent() {
        GiftCertificate newCertificate = new GiftCertificate(null, "name", "description", BigDecimal.valueOf(241.45), "1", null, null, null);

        Mockito.when(giftCertificatesRepository.getGiftCertificateById(1L)).thenReturn(Optional.of(newCertificate));
        Mockito.when(giftCertificatesRepository.updateGiftCertificateById(1L, newCertificate)).thenReturn(Optional.empty());

        assertThrows(CertificateNotRepresent.class, () -> giftCertificatesService.updateGiftCertificate(1L, newCertificate));
    }

    @Test
    public void updateGiftCertificateByIdShouldReturnCertificate() {
        GiftCertificate oldCertificate = new GiftCertificate(4L, "testName", "testDescription",
                BigDecimal.valueOf(125.25), "15", LocalDateTime.now().toString(), null, List.of(new Tag(2L, "testTag1"), new Tag(3L, "AnotherTagName")));

        GiftCertificate updateCertificate = new GiftCertificate(null, "updateName", null,
                BigDecimal.valueOf(355.55), "25", null, LocalDateTime.now().toString(), null);

        GiftCertificate expectedCertificate = new GiftCertificate(oldCertificate.getId(), updateCertificate.getName(),
                oldCertificate.getDescription(), updateCertificate.getPrice(), updateCertificate.getDuration(),
                oldCertificate.getCreateDate(), updateCertificate.getLastUpdateDate(),
                oldCertificate.getTagsList());


        Mockito.when(giftCertificatesRepository.getGiftCertificateById(oldCertificate.getId())).thenReturn(Optional.of(oldCertificate));
        Mockito.when(giftCertificatesRepository.updateGiftCertificateById(oldCertificate.getId(), updateCertificate)).thenReturn(Optional.of(expectedCertificate));
        assertEquals(expectedCertificate, giftCertificatesService.updateGiftCertificate(oldCertificate.getId(), updateCertificate));
    }
}
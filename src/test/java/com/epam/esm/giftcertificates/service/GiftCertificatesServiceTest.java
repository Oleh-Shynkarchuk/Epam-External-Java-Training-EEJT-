package com.epam.esm.giftcertificates.service;

import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.exception.CertificateNotRepresent;
import com.epam.esm.giftcertificates.exception.CertificateInvalidRequest;
import com.epam.esm.giftcertificates.exception.CertificateNotFound;
import com.epam.esm.integration.sqlrepo.MySQLRepository;
import com.epam.esm.integration.sqlrepo.SQLQuery;
import com.epam.esm.tags.entity.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GiftCertificatesServiceTest {

    @Mock
    private MySQLRepository giftCertificatesRepository;

    @InjectMocks
    private GiftCertificatesServiceImpl giftCertificatesService;

    @Test
    public void readGiftCertificateByMapOfRequestParamShouldThrowInvalidRequest() {
        assertThrows(CertificateInvalidRequest.class, () -> giftCertificatesService.readGiftCertificate(Map.of("tag_name", "")));

        assertThrows(CertificateInvalidRequest.class, () -> giftCertificatesService.readGiftCertificate(Map.of("gift_name", "         ")));

        assertThrows(CertificateInvalidRequest.class, () -> giftCertificatesService.readGiftCertificate(Map.of("description", " ")));

        assertThrows(CertificateInvalidRequest.class, () -> giftCertificatesService.readGiftCertificate(Map.of("sort_date", "notASCorDESC")));

        assertThrows(CertificateInvalidRequest.class, () -> giftCertificatesService.readGiftCertificate(Map.of("sort_name", "ergergtg")));
    }

    @Test
    public void readGiftCertificateByMapOfRequestParamShouldThrowItemNotFound() {

        Mockito.when(giftCertificatesRepository.getGiftCertificateByParam(
                SQLQuery.BuildQuery.SEARCH_BASE_QUERY + SQLQuery.BuildQuery.SEARCH_TAG_NAME_QUERY_PART,
                List.of("tag1"))).thenReturn(Optional.empty());

        assertThrows(CertificateNotFound.class, () -> giftCertificatesService.readGiftCertificate(Map.of("tag_name", "tag1")));
    }

    @Test
    public void readGiftCertificateByMapOfRequestParamShouldReturnCertificatesWithTags() {

        GiftCertificate expected = new GiftCertificate(4L, "testCertfName", "testDescription",
                BigDecimal.valueOf(125.25), "15", LocalDateTime.now().toString(), null, List.of(new Tag(2L, "testTag1"), new Tag(3L, "AnotherTagName")));

        Mockito.when(giftCertificatesRepository.getGiftCertificateByParam(SQLQuery.BuildQuery.SEARCH_BASE_QUERY
                + SQLQuery.BuildQuery.SEARCH_TAG_NAME_QUERY_PART + SQLQuery.BuildQuery.AND_DESCRIPTION_LIKE, List.of("testTag1", "%script%"))).thenReturn(Optional.of(List.of(expected)));

        assertEquals(List.of(expected), giftCertificatesService.readGiftCertificate(Map.of("tag_name", "testTag1", "description", "script")));
    }

    @Test
    public void readAllGiftCertificateShouldThrowItemNotFound() {

        Mockito.when(giftCertificatesRepository.getAllGiftCertificates()).thenReturn(Optional.empty());

        assertThrows(CertificateNotFound.class, () -> giftCertificatesService.readAllGiftCertificates());
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

        assertThrows(CertificateInvalidRequest.class, () -> giftCertificatesService.createGiftCertificate(nameExist));
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

        assertThrows(CertificateInvalidRequest.class, () -> giftCertificatesService.deleteGiftCertificate(0L));

        assertThrows(CertificateInvalidRequest.class, () -> giftCertificatesService.deleteGiftCertificate(-10L));
    }

    @Test
    public void deleteGiftCertificateShouldThrowNotFound() {

        long deleteId = 1L;
        Mockito.when(giftCertificatesRepository.getGiftCertificateById(deleteId)).thenReturn(Optional.empty());

        assertThrows(CertificateNotFound.class, () -> giftCertificatesService.deleteGiftCertificate(deleteId));
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
        assertThrows(CertificateInvalidRequest.class, () -> giftCertificatesService.readGiftCertificate(0L));

        assertThrows(CertificateInvalidRequest.class, () -> giftCertificatesService.readGiftCertificate(-4L));
    }

    @Test
    public void readGiftCertificateByIdShouldReturnItemNotFoundExceptionWhenCertificateDoesNotExist() {

        Mockito.when(giftCertificatesRepository.getGiftCertificateById(1L)).thenReturn(Optional.empty());

        assertThrows(CertificateNotFound.class, () -> giftCertificatesService.readGiftCertificate(1L));
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

        assertThrows(CertificateInvalidRequest.class, () -> giftCertificatesService.updateGiftCertificate(negativeId, newCertificate));

        assertThrows(CertificateInvalidRequest.class, () -> giftCertificatesService.updateGiftCertificate(zeroId, newCertificate));

    }

    @Test
    public void updateGiftCertificateShouldReturnInvalidRequestCuzNegativeOrZeroPriceOrNegativeDurationOrInconvertible() {
        long positiveId = 5L;
        GiftCertificate newCertificateNegativePrice = new GiftCertificate(null, "name", "description", BigDecimal.valueOf(-241.45), "15", null, null, null);

        GiftCertificate newCertificateNegativeDuration = new GiftCertificate(null, "name", "description", BigDecimal.valueOf(541.45), "-15", null, null, null);

        assertThrows(CertificateInvalidRequest.class, () -> giftCertificatesService.updateGiftCertificate(positiveId, newCertificateNegativePrice));
        assertThrows(CertificateInvalidRequest.class, () -> giftCertificatesService.updateGiftCertificate(positiveId, newCertificateNegativeDuration));

    }

    @Test
    public void updateGiftCertificateShouldReturnNotFound() {
        long positiveId = 5L;
        GiftCertificate newCertificate = new GiftCertificate(null, "name", "description", BigDecimal.valueOf(241.45), "1", null, null, null);

        Mockito.when(giftCertificatesRepository.getGiftCertificateById(positiveId)).thenReturn(Optional.empty());

        assertThrows(CertificateNotFound.class, () -> giftCertificatesService.updateGiftCertificate(positiveId, newCertificate));
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
                oldCertificate.getCreate_date(), updateCertificate.getLast_update_date(),
                oldCertificate.getTagsList());


        Mockito.when(giftCertificatesRepository.getGiftCertificateById(oldCertificate.getId())).thenReturn(Optional.of(oldCertificate));
        Mockito.when(giftCertificatesRepository.updateGiftCertificateById(oldCertificate.getId(), expectedCertificate)).thenReturn(Optional.of(expectedCertificate));

        assertEquals(expectedCertificate, giftCertificatesService.updateGiftCertificate(oldCertificate.getId(), updateCertificate));
    }
}
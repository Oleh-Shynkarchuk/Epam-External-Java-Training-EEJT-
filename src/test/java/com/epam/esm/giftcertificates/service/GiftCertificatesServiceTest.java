package com.epam.esm.giftcertificates.service;

import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.integration.errorhandle.InternalDatabaseException;
import com.epam.esm.integration.errorhandle.InvalidRequest;
import com.epam.esm.integration.errorhandle.ItemNotFound;
import com.epam.esm.integration.sqlrepo.MySQLRepository;
import com.epam.esm.integration.sqlrepo.SQLQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GiftCertificatesServiceTest {

    @Mock
    private MySQLRepository giftCertificatesRepository;

    @InjectMocks
    private GiftCertificatesServiceImpl giftCertificatesService;


    @Test
    public void shouldThrowInvalidRequestExceptionIfIDNegativeOrZero(){
//        Mockito.when(giftCertificatesRepository.getGiftCertificateById(-1L)).thenReturn(Optional.of(new GiftCertificate()));
        assertThrows(InvalidRequest.class,()->giftCertificatesService.readGiftCertificate(0L));

        assertThrows(InvalidRequest.class,()->giftCertificatesService.deleteGiftCertificate(0L));

        assertThrows(InvalidRequest.class,()->giftCertificatesService.updateGiftCertificate(0L,new GiftCertificate()));

        assertThrows(InvalidRequest.class,()->giftCertificatesService.readGiftCertificate(Map.of("tag_name","")));

        GiftCertificate newCertificate= new GiftCertificate(null,"name","description",BigDecimal.valueOf(241.45),"1",null,null,null);

        Mockito.when(giftCertificatesRepository.isGiftCertificateByNameExist(newCertificate.getName())).thenReturn(true);

        assertThrows(InvalidRequest.class,()->giftCertificatesService.createGiftCertificate(newCertificate));


    }

    @Test
    public void shouldReturnItemNotFoundExceptionIfCertificateDoesNotExist(){
//        GiftCertificate expectedCertificate = new GiftCertificate(1L,"GiftName","description",BigDecimal.valueOf(243.12),"",List.of());
        Mockito.when(giftCertificatesRepository.getGiftCertificateById(1L)).thenReturn(Optional.empty());
        assertThrows(ItemNotFound.class,()->giftCertificatesService.readGiftCertificate(1L));

        Mockito.when(giftCertificatesRepository.getAllGiftCertificates()).thenReturn(Optional.empty());
        assertThrows(ItemNotFound.class,()->giftCertificatesService.readAllGiftCertificates());

        Mockito.when(giftCertificatesRepository.getGiftCertificateById(1L)).thenReturn(Optional.empty());
        assertThrows(ItemNotFound.class,()->giftCertificatesService.deleteGiftCertificate(1L));

        Mockito.when(giftCertificatesRepository.getGiftCertificateById(1L)).thenReturn(Optional.empty());
        assertThrows(ItemNotFound.class,()->giftCertificatesService.updateGiftCertificate(1L,new GiftCertificate()));

        Mockito.when(giftCertificatesRepository.getGiftCertificateByParam(
                SQLQuery.BuildQuery.SEARCH_BASE_QUERY+SQLQuery.BuildQuery.SEARCH_TAG_NAME_QUERY_PART,
                List.of("tag1"))).thenReturn(Optional.empty());
        assertThrows(ItemNotFound.class,()->giftCertificatesService.readGiftCertificate(Map.of("tag_name","tag1")));

    }
    @Test
    public void shouldReturnInternalDatabaseExceptionIfCertificateWasNotFoundAfterChanges(){
        GiftCertificate newCertificate= new GiftCertificate(null,"name","description",BigDecimal.valueOf(241.45),"1",null,null,null);

        Mockito.when(giftCertificatesRepository.isGiftCertificateByNameExist(newCertificate.getName())).thenReturn(false);

        assertThrows(InternalDatabaseException.class,()->giftCertificatesService.createGiftCertificate(newCertificate));

        Mockito.when(giftCertificatesRepository.getGiftCertificateById(1L)).thenReturn(Optional.of(newCertificate));
        Mockito.when(giftCertificatesRepository.deleteGiftCertificateById(1L)).thenReturn(false);

        assertThrows(InternalDatabaseException.class,()->giftCertificatesService.deleteGiftCertificate(1L));

        Mockito.when(giftCertificatesRepository.getGiftCertificateById(1L)).thenReturn(Optional.of(newCertificate));
        Mockito.when(giftCertificatesRepository.updateGiftCertificateById(1L,newCertificate)).thenReturn(Optional.empty());

        assertThrows(InternalDatabaseException.class,()->giftCertificatesService.updateGiftCertificate(1L,newCertificate));

    }
}
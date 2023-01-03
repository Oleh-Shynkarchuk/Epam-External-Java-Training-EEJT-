package com.epam.esm.giftcertificates.service;

import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.integration.errorhandle.InvalidRequest;
import com.epam.esm.integration.sqlrepo.MySQLRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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

    }

    @Test
    public void shouldReturnNoSuchElementExceptionIfCertificateDoesNotExist(){
        GiftCertificate expectedCertificate = new GiftCertificate(1L,"GiftName","description",
                BigDecimal.valueOf(243.12),"",List.of());
        Mockito.when(giftCertificatesRepository.getGiftCertificateById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class,()->giftCertificatesService.readGiftCertificate(1L));

        Mockito.when(giftCertificatesRepository.getAllGiftCertificates()).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class,()->giftCertificatesService.readAllGiftCertificates());

        Mockito.when(giftCertificatesRepository.getGiftCertificateById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class,()->giftCertificatesService.deleteGiftCertificate(1L));

        Mockito.when(giftCertificatesRepository.getAllGiftCertificates()).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class,()->giftCertificatesService.updateGiftCertificate(1L,new GiftCertificate()));
    }
}
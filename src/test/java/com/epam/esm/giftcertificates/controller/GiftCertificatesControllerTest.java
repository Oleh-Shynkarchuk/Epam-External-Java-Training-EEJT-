package com.epam.esm.giftcertificates.controller;

import com.epam.esm.giftcertificates.service.GiftCertificatesService;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class GiftCertificatesControllerTest {
    @Mock
    private GiftCertificatesService giftCertificatesService;
    @InjectMocks
    private GiftCertificatesController giftCertificatesController;

    JsonMapper jsonMapper = new JsonMapper();

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(giftCertificatesController).build();
    }

    @Test
    void getAllGiftCertificates() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/gifts"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

        Mockito.verify(giftCertificatesService).readAllGiftCertificates();
    }

    @Test
    void getGiftCertificateById() {
    }

    @Test
    void searchGiftCertificates() {
    }

    @Test
    void createNewGiftCertificate() {
    }

    @Test
    void patchGiftCertificate() {
    }

    @Test
    void deleteGiftCertificateById() {
    }
}
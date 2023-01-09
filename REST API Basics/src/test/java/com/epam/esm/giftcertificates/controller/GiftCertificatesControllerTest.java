package com.epam.esm.giftcertificates.controller;

import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.filter.entity.SearchParamsBuilder;
import com.epam.esm.giftcertificates.service.GiftCertificatesService;
import com.epam.esm.tags.entity.Tag;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(MockitoExtension.class)
class GiftCertificatesControllerTest {
    @Mock
    private GiftCertificatesService giftCertificatesService;
    @Mock
    private SearchParamsBuilder searchParamsBuilder;
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
    void getGiftCertificateById() throws Exception {
        long id = 1L;
        GiftCertificate expected = new GiftCertificate(id, "testName", "testDescription",
                BigDecimal.valueOf(14.32), "15", LocalDateTime.now().toString(),
                LocalDateTime.now().toString(), List.of());

        when(giftCertificatesService.readGiftCertificate(id)).thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders.get("/gifts/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(jsonMapper.writeValueAsString(expected)));

        Mockito.verify(giftCertificatesService).readGiftCertificate(id);
    }

    @Test
    void searchGiftCertificates() throws Exception {
        long id = 1L;
        GiftCertificate expected = new GiftCertificate(id, "testName", "testDescription",
                BigDecimal.valueOf(14.32), "15", LocalDateTime.now().toString(),
                LocalDateTime.now().toString(), List.of(new Tag(id, "testTag")));

        when(searchParamsBuilder.setTagName("testTag")).thenReturn(searchParamsBuilder);
        when(searchParamsBuilder.setGiftName(null)).thenReturn(searchParamsBuilder);
        when(searchParamsBuilder.setDescription(null)).thenReturn(searchParamsBuilder);
        when(searchParamsBuilder.setSortDate(null)).thenReturn(searchParamsBuilder);
        when(searchParamsBuilder.setSortName(null)).thenReturn(searchParamsBuilder);
        when(giftCertificatesService.readGiftCertificate(searchParamsBuilder.setTagName("testTag")
                .create())).thenReturn(List.of(expected));
        mockMvc.perform(MockMvcRequestBuilders.get("/gifts/search?tagName=testTag"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(jsonMapper.writeValueAsString(List.of(expected))));

        Mockito.verify(giftCertificatesService).readGiftCertificate(searchParamsBuilder.setTagName("testTag")
                .create());
    }

    @Test
    void createNewGiftCertificate() throws Exception {

        GiftCertificate expected = new GiftCertificate(null, "testName", "testDescription",
                BigDecimal.valueOf(14.32), "15", LocalDateTime.now().toString(),
                LocalDateTime.now().toString(), List.of());
        GiftCertificate actual = new GiftCertificate(1L, "testName", "testDescription",
                BigDecimal.valueOf(14.32), "15", LocalDateTime.now().toString(),
                LocalDateTime.now().toString(), List.of());

        String body = jsonMapper.writeValueAsString(expected);
        String response = jsonMapper.writeValueAsString(actual);

        when(giftCertificatesService.createGiftCertificate(expected)).thenReturn(actual);

        mockMvc.perform(MockMvcRequestBuilders.post("/gifts")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(response));

        Mockito.verify(giftCertificatesService).createGiftCertificate(expected);
    }

    @Test
    void patchGiftCertificate() throws Exception {
        long id = 3L;
        GiftCertificate oldCertificate = new GiftCertificate(id, "testName", "testDescription",
                BigDecimal.valueOf(14.32), "15", LocalDateTime.now().toString(),
                LocalDateTime.now().toString(), List.of());
        GiftCertificate newCertificate = new GiftCertificate(id, null, "updateDescription",
                BigDecimal.valueOf(35.32), null, null,
                null, List.of(new Tag(id, "updateTag")));
        GiftCertificate expected = new GiftCertificate(id, oldCertificate.getName(), "updateDescription",
                newCertificate.getPrice(), oldCertificate.getDuration(), null,
                null, newCertificate.getTagsList());
        String body = jsonMapper.writeValueAsString(newCertificate);
        String response = jsonMapper.writeValueAsString(expected);

        when(giftCertificatesService.updateGiftCertificate(id, newCertificate)).thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders.patch("/gifts/" + id)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(response));

        Mockito.verify(giftCertificatesService).updateGiftCertificate(id, newCertificate);
    }

    @Test
    public void ShouldReturnNoContentStatusWhenDeleteCertificate() throws Exception {
        long id = 1L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/gifts/" + id))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(giftCertificatesService).deleteGiftCertificate(id);
    }
}
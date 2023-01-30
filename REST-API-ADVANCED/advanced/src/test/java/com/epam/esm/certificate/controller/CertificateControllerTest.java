package com.epam.esm.certificate.controller;

import com.epam.esm.certificate.entity.Certificate;
import com.epam.esm.certificate.service.CertificateServiceImpl;
import com.epam.esm.errorhandle.constants.ErrorConstants;
import com.epam.esm.errorhandle.controller.ErrorController;
import com.epam.esm.errorhandle.validation.Validate;
import com.epam.esm.hateoas.HateoasSupport;
import com.epam.esm.tag.entity.Tag;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(MockitoExtension.class)
class CertificateControllerTest {
    @Mock
    private HateoasSupport hateoasSupport;
    @Mock
    private Validate validate;
    @Mock
    private CertificateServiceImpl certificateService;
    @InjectMocks
    private CertificateController certificateController;

    private final JsonMapper jsonMapper = new JsonMapper();

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.standaloneSetup(certificateController)
                .setControllerAdvice(new ErrorController())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void getAllGiftCertificates() throws Exception {
        long id = 1L;
        Pageable pageable = PageRequest.of(0, 20);
        Tag firstTag = Tag.builder().id(1L).name("TestTag1").build();
        Tag secondTag = Tag.builder().id(2L).name("TestTag2").build();
        List<Tag> tags = List.of(firstTag, secondTag);
        Certificate certificate1 = Certificate.builder().id(id).name("TestCertificate1")
                .description("test description").price(BigDecimal.valueOf(200))
                .lastUpdateDate(LocalDateTime.now().toString())
                .durationOfDays("25").tags(tags).build();
        Certificate certificate2 = Certificate.builder().id(id + 1).name("TestCertificate2")
                .description("UPDATED DESCRIPTION").price(BigDecimal.valueOf(200))
                .lastUpdateDate(LocalDateTime.now().toString())
                .durationOfDays("25").tags(tags).build();
        List<Certificate> certificateList = List.of(certificate1, certificate2);
        CollectionModel<Certificate> certificateCollectionModel = CollectionModel.of(certificateList);
        String expected = jsonMapper.writeValueAsString(certificateCollectionModel);

        Mockito.when(certificateService.getAllCertificates(pageable))
                .thenReturn(certificateList);
        Mockito.when(hateoasSupport.addHateoasSupportToCertificateList(certificateList, pageable))
                .thenReturn(certificateCollectionModel);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/v1/api/certificate"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        assertEquals(response, expected);
        Mockito.verify(certificateService).getAllCertificates(pageable);
        Mockito.verify(hateoasSupport).addHateoasSupportToCertificateList(certificateList, pageable);
    }

    @Test
    void getCertificateById() throws Exception {
        long id = 1L;
        Tag firstTag = Tag.builder().id(1L).name("TestTag1").build();
        Tag secondTag = Tag.builder().id(2L).name("TestTag2").build();
        List<Tag> tags = List.of(firstTag, secondTag);
        Certificate expectedCertificate = Certificate.builder().id(id).name("TestCertificate1")
                .description("UPDATED DESCRIPTION").price(BigDecimal.valueOf(200))
                .lastUpdateDate(LocalDateTime.now().toString())
                .durationOfDays("25").tags(tags).build();
        Mockito.when(validate.isPositiveAndParsableId(String.valueOf(id))).thenReturn(true);
        Mockito.when(certificateService.getCertificateById(id)).thenReturn(expectedCertificate);
        Mockito.when(hateoasSupport.addHateoasSupportToSingleCertificate(expectedCertificate)).thenReturn(expectedCertificate);
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/v1/api/certificate/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        String expected = jsonMapper.writeValueAsString(expectedCertificate);

        assertEquals(response, expected);
        Mockito.verify(validate).isPositiveAndParsableId(String.valueOf(id));
        Mockito.verify(certificateService).getCertificateById(id);
    }

    @Test
    void getCertificateShouldThrowInvalidRequest() throws Exception {
        long id = -1L;
        Mockito.when(validate.isPositiveAndParsableId(String.valueOf(id))).thenReturn(false);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/v1/api/certificate/-1"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        String expected = jsonMapper.writeValueAsString(Map.of("errorCode", ErrorConstants.CERTIFICATE_INVALID_REQUEST_ERROR_CODE,
                "errorMessage", "Invalid input ( id = " + id
                        + " ). Only a positive number is allowed ( 1 and more )."));
        assertEquals(response, expected);
        Mockito.verify(validate).isPositiveAndParsableId(String.valueOf(id));
    }


    @Test
    void getCertificateBySeveralTagsName() throws Exception {
        long id = 1L;
        Pageable pageable = PageRequest.of(0, 20);
        Tag firstTag = Tag.builder().id(1L).name("TestTag1").build();
        Tag secondTag = Tag.builder().id(2L).name("TestTag2").build();
        List<Tag> tags = List.of(firstTag, secondTag);
        Certificate certificate1 = Certificate.builder().id(id).name("TestCertificate1")
                .description("test description").price(BigDecimal.valueOf(200))
                .lastUpdateDate(LocalDateTime.now().toString())
                .durationOfDays("25").tags(tags).build();
        Certificate certificate2 = Certificate.builder().id(id + 1).name("TestCertificate2")
                .description("test description").price(BigDecimal.valueOf(200))
                .lastUpdateDate(LocalDateTime.now().toString())
                .durationOfDays("25").tags(tags).build();
        Certificate certificate3 = Certificate.builder().id(id + 2).name("TestCertificate3")
                .description("test description").price(BigDecimal.valueOf(200))
                .lastUpdateDate(LocalDateTime.now().toString())
                .durationOfDays("25").tags(null).build();
        List<Certificate> certificateList = List.of(certificate1, certificate2);
        CollectionModel<Certificate> certificateCollectionModel = CollectionModel.of(certificateList);
        String expected = jsonMapper.writeValueAsString(certificateCollectionModel);
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("page", "0");
        multiValueMap.add("size", "20");

        Mockito.when(certificateService.getCertificateByTagsName(pageable, List.of("testTag2")))
                .thenReturn(certificateList);
        Mockito.when(hateoasSupport.addHateoasSupport(certificateList, pageable, List.of("testTag2")))
                .thenReturn(certificateCollectionModel);
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/v1/api/certificate/search")
                        .params(multiValueMap)
                        .param("tagName", "testTag2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertEquals(response, expected);
        Mockito.verify(certificateService).getCertificateByTagsName(pageable, List.of("testTag2"));
        Mockito.verify(hateoasSupport).addHateoasSupport(certificateList, pageable, List.of("testTag2"));
    }

    @Test
    void createCertificate() throws Exception {
        Tag firstTag = Tag.builder().id(1L).name("TestTag1").build();
        Tag secondTag = Tag.builder().id(2L).name("TestTag2").build();
        List<Tag> tags = List.of(firstTag, secondTag);
        Certificate newCertificate = Certificate.builder().id(null).name("TestCertificate1")
                .description("UPDATED DESCRIPTION").price(BigDecimal.valueOf(200))
                .createDate(LocalDateTime.now().toString())
                .lastUpdateDate(LocalDateTime.now().toString())
                .durationOfDays("25").tags(tags).build();
        Certificate created = Certificate.builder().id(1L).name("TestCertificate1")
                .description("UPDATED DESCRIPTION").price(BigDecimal.valueOf(200))
                .createDate(LocalDateTime.now().toString())
                .lastUpdateDate(LocalDateTime.now().toString())
                .durationOfDays("25").tags(tags).build();

        String expected = jsonMapper.writeValueAsString(created);

        Mockito.when(certificateService.createCertificate(newCertificate)).thenReturn(created);
        Mockito.when(hateoasSupport.addHateoasSupportToSingleCertificate(created)).thenReturn(created);

        String response = mockMvc.perform(MockMvcRequestBuilders.post("/v1/api/certificate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newCertificate)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        assertEquals(response, expected);
        Mockito.verify(certificateService).createCertificate(newCertificate);
        Mockito.verify(hateoasSupport).addHateoasSupportToSingleCertificate(created);
    }

    @Test
    void patchCertificate() throws Exception {
        long id = 1L;
        Tag firstTag = Tag.builder().id(id).name("TestTag1").build();
        Tag secondTag = Tag.builder().id(2L).name("TestTag2").build();
        List<Tag> tags = List.of(firstTag, secondTag);
        Certificate newCertificate = Certificate.builder().id(null).name("TestCertificate1")
                .description("UPDATED DESCRIPTION").price(BigDecimal.valueOf(200))
                .createDate(LocalDateTime.now().toString())
                .lastUpdateDate(LocalDateTime.now().toString())
                .durationOfDays("25").tags(tags).build();
        Certificate patched = Certificate.builder().id(id).name("TestCertificate1")
                .description("UPDATED DESCRIPTION").price(BigDecimal.valueOf(200))
                .createDate(LocalDateTime.now().toString())
                .lastUpdateDate(LocalDateTime.now().toString())
                .durationOfDays("25").tags(tags).build();

        String expected = jsonMapper.writeValueAsString(patched);

        Mockito.when(validate.isPositiveAndParsableId(String.valueOf(id))).thenReturn(true);
        Mockito.when(certificateService.patchCertificate(id, newCertificate)).thenReturn(patched);
        Mockito.when(hateoasSupport.addHateoasSupportToSingleCertificate(patched)).thenReturn(patched);

        String response = mockMvc.perform(MockMvcRequestBuilders.patch("/v1/api/certificate/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newCertificate)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        assertEquals(response, expected);
        Mockito.verify(validate).isPositiveAndParsableId(String.valueOf(id));
        Mockito.verify(certificateService).patchCertificate(id, newCertificate);
        Mockito.verify(hateoasSupport).addHateoasSupportToSingleCertificate(patched);
    }

    @Test
    void patchCertificateShouldThrowInvalidRequest() throws Exception {
        long id = -1L;
        Certificate newCertificate = Certificate.builder().id(null).name("TestCertificate1")
                .description("UPDATED DESCRIPTION").price(BigDecimal.valueOf(200))
                .createDate(LocalDateTime.now().toString())
                .lastUpdateDate(LocalDateTime.now().toString())
                .durationOfDays("25").tags(null).build();

        Mockito.when(validate.isPositiveAndParsableId(String.valueOf(id))).thenReturn(false);

        String response = mockMvc.perform(MockMvcRequestBuilders.patch("/v1/api/certificate/-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newCertificate)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        String expected = jsonMapper.writeValueAsString(Map.of("errorCode", ErrorConstants.CERTIFICATE_INVALID_REQUEST_ERROR_CODE,
                "errorMessage", "Invalid input ( id = " + id
                        + " ). Only a positive number is allowed ( 1 and more )."));
        assertEquals(response, expected);
        Mockito.verify(validate).isPositiveAndParsableId(String.valueOf(id));
    }

    @Test
    void deleteCertificateById() throws Exception {
        long id = 1L;
        Mockito.when(validate.isPositiveAndParsableId(String.valueOf(id))).thenReturn(true);
        Mockito.doNothing().when(certificateService).deleteCertificateById(id);
        String response = mockMvc.perform(MockMvcRequestBuilders.delete("/v1/api/certificate/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andReturn().getResponse().getContentAsString();

        assertTrue(response.isEmpty());
        Mockito.verify(validate).isPositiveAndParsableId(String.valueOf(id));
        Mockito.verify(certificateService).deleteCertificateById(id);
    }

    @Test
    void deleteCertificateByIdShouldThrowInvalidRequest() throws Exception {
        long id = -1L;
        Mockito.when(validate.isPositiveAndParsableId(String.valueOf(id))).thenReturn(false);

        String response = mockMvc.perform(MockMvcRequestBuilders.delete("/v1/api/certificate/-1"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        String expected = jsonMapper.writeValueAsString(Map.of("errorCode", ErrorConstants.CERTIFICATE_INVALID_REQUEST_ERROR_CODE,
                "errorMessage", "Invalid input ( id = " + id
                        + " ). Only a positive number is allowed ( 1 and more )."));
        assertEquals(response, expected);
        Mockito.verify(validate).isPositiveAndParsableId(String.valueOf(id));
    }
}
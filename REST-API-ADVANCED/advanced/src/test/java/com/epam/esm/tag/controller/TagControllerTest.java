package com.epam.esm.tag.controller;

import com.epam.esm.ErrorConstants;
import com.epam.esm.ErrorController;
import com.epam.esm.tag.entity.Tag;
import com.epam.esm.tag.hateoas.TagHateoasSupport;
import com.epam.esm.tag.service.TagServiceImpl;
import com.epam.esm.tag.validation.TagValidator;
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

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class TagControllerTest {

    @Mock
    private TagHateoasSupport hateoasSupport;
    @Mock
    private TagValidator validator;
    @Mock
    private TagServiceImpl tagService;
    @InjectMocks
    private TagController tagController;

    private final JsonMapper jsonMapper = new JsonMapper();

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.standaloneSetup(tagController)
                .setControllerAdvice(new ErrorController())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void getAllTag() throws Exception {
        Pageable pageable = PageRequest.of(0, 20);
        Tag tag1 = Tag.builder().id("1").name("testTag1").build();
        Tag tag2 = Tag.builder().id("2").name("testTag2").build();
        Tag tag3 = Tag.builder().id("3").name("testTag3").build();
        List<Tag> tags = List.of(tag1, tag2, tag3);
        String expected = jsonMapper.writeValueAsString(CollectionModel.of(tags));

        Mockito.when(tagService.getAllTags(pageable))
                .thenReturn(tags);
        Mockito.when(hateoasSupport.addHateoasSupportToAllTag(tags, pageable))
                .thenReturn(CollectionModel.of(tags));

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/v1/api/tag"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        assertEquals(response, expected);
        Mockito.verify(tagService).getAllTags(pageable);
        Mockito.verify(hateoasSupport).addHateoasSupportToAllTag(tags, pageable);
    }

    @Test
    void getTagById() throws Exception {
        String request = "1";
        Tag tag1 = Tag.builder().id("1").name("testTag1").build();
        String expected = jsonMapper.writeValueAsString(tag1);

        Mockito.when(validator.isPositiveAndParsableIdResponse(request)).thenReturn("");
        Mockito.when(tagService.getTagById(request))
                .thenReturn(tag1);
        Mockito.when(hateoasSupport.addHateoasSupportToSingleTag(tag1))
                .thenReturn(tag1);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/v1/api/tag/" + request))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        assertEquals(response, expected);
    }

    @Test
    void getTagByIdShouldReturnInvalidRequest() throws Exception {
        long request = -1L;
        String expected = jsonMapper.writeValueAsString(Map.of("errorCode", ErrorConstants.TAG_INVALID_REQUEST_ERROR_CODE,
                "errorMessage", "Invalid input ( id = " + request
                        + " ). Only a positive number is allowed ( 1 and more )."));

        Mockito.when(validator.isPositiveAndParsableIdResponse(String.valueOf(request)))
                .thenReturn("Invalid input ( id = " + request
                        + " ). Only a positive number is allowed ( 1 and more ).");

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/v1/api/tag/" + request))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        assertEquals(response, expected);
    }

    @Test
    void getMostWidelyUsedTag() throws Exception {
        Tag tag1 = Tag.builder().id("1").name("testTag1").build();
        String expected = jsonMapper.writeValueAsString(tag1);

        Mockito.when(tagService.getMostWidelyUsedTag())
                .thenReturn(tag1);
        Mockito.when(hateoasSupport.addHateoasSupportToSingleTag(tag1))
                .thenReturn(tag1);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/v1/api/tag/best"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        assertEquals(response, expected);
    }

    @Test
    void createTag() throws Exception {
        Tag requestTag = Tag.builder().name("testTag1").build();
        Tag expectedTag = Tag.builder().id("1").name("testTag1").build();
        String expected = jsonMapper.writeValueAsString(expectedTag);

        Mockito.when(validator.isCreatableTagFieldsErrorResponse(requestTag)).thenReturn("");
        Mockito.when(tagService.createTag(requestTag))
                .thenReturn(expectedTag);
        Mockito.when(hateoasSupport.addHateoasSupportToSingleTag(expectedTag))
                .thenReturn(expectedTag);

        String response = mockMvc.perform(MockMvcRequestBuilders.post("/v1/api/tag")
                        .contentType(MediaType.APPLICATION_JSON).content(jsonMapper.writeValueAsString(requestTag)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        assertEquals(response, expected);
    }

    @Test
    void createTagShouldReturnInvalidRequest() throws Exception {
        Tag tag1 = Tag.builder().name("").build();
        String expected = jsonMapper.writeValueAsString(Map.of("errorCode", ErrorConstants.TAG_INVALID_REQUEST_ERROR_CODE,
                "errorMessage", "Invalid input ( name = " + tag1.getName()
                        + " ). Name can not be empty."));

        Mockito.when(validator.isCreatableTagFieldsErrorResponse(tag1))
                .thenReturn("Invalid input ( name = " + tag1.getName()
                        + " ). Name can not be empty.");
        String response = mockMvc.perform(MockMvcRequestBuilders.post("/v1/api/tag")
                        .contentType(MediaType.APPLICATION_JSON).content(jsonMapper.writeValueAsString(tag1)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        assertEquals(response, expected);
    }

    @Test
    void deleteTag() throws Exception {
        String id = "1";
        Mockito.when(validator.isPositiveAndParsableIdResponse(id)).thenReturn("");
        Mockito.doNothing().when(tagService).deleteTagById(id);
        String response = mockMvc.perform(MockMvcRequestBuilders.delete("/v1/api/tag/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andReturn().getResponse().getContentAsString();

        assertTrue(response.isEmpty());
        Mockito.verify(validator).isPositiveAndParsableIdResponse(id);
        Mockito.verify(tagService).deleteTagById(id);
    }

    @Test
    void deleteTagShouldReturnInvalidRequest() throws Exception {
        long id = -1L;
        String expected = jsonMapper.writeValueAsString(Map.of("errorCode", ErrorConstants.TAG_INVALID_REQUEST_ERROR_CODE,
                "errorMessage", "Invalid input ( id = " + id
                        + " ). Only a positive number is allowed ( 1 and more )."));

        Mockito.when(validator.isPositiveAndParsableIdResponse(String.valueOf(id)))
                .thenReturn("Invalid input ( id = " + id
                        + " ). Only a positive number is allowed ( 1 and more ).");

        String response = mockMvc.perform(MockMvcRequestBuilders.delete("/v1/api/tag/-1"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        assertEquals(response, expected);
        Mockito.verify(validator).isPositiveAndParsableIdResponse(String.valueOf(id));
    }
}
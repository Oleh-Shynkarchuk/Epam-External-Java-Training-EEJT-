package com.epam.esm.tags.controller;

import com.epam.esm.tags.entity.Tag;
import com.epam.esm.tags.service.TagsService;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(MockitoExtension.class)
public class TagsControllerTest {
    @Mock
    private TagsService tagsService;
    @InjectMocks
    private TagsController tagsController;

    private final JsonMapper jsonMapper = new JsonMapper();
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(tagsController).build();
    }

    @Test
    public void shouldReturnJSONWhenGetTagsListAndOKStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tags"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
        Mockito.verify(tagsService).readAllTags();
    }

    @Test
    public void ShouldReturnJSONWhenGetTagByIdAndOKStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tags/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void ShouldReturnJSONofTagAndOKStatusWhenPostNewTag() throws Exception {
        Tag requestTag = new Tag(null, "Tag");
        Tag expected = new Tag(1L, "Tag");

        String request = jsonMapper.writeValueAsString(requestTag);
        String response = jsonMapper.writeValueAsString(expected);

        when(tagsService.createTag(requestTag)).thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders.post("/tags")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(response));

        Mockito.verify(tagsService).createTag(requestTag);
    }

    @Test
    public void ShouldReturnNoContentStatusWhenDeleteTag() throws Exception {
        long id = 1L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/tags/" + id))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(tagsService).deleteTag(id);
    }
}
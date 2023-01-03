package com.epam.esm.giftcertificates.controller;

import com.epam.esm.tags.entity.Tag;

import com.epam.esm.tags.controller.TagsController;
import com.epam.esm.tags.service.TagsService;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
public class TagsControllerTest {
    @Mock
    private TagsService tagsService;
    @InjectMocks
    private TagsController tagsController;
    JsonMapper jsonMapper=new JsonMapper();

    private MockMvc mockMvc;
    @BeforeEach
    public void setUp() {
        mockMvc= MockMvcBuilders.standaloneSetup(tagsController).build();
    }
    @Test
    public void shouldReturnJSONWhenGetTagsListAndOKStatus() throws Exception {
        List<Tag> tagList =new ArrayList<>();
        tagList.add(new Tag(1L,"Tag1"));
        tagList.add(new Tag(2L,"Tag2"));
        String response = jsonMapper.writeValueAsString(tagList);
        doReturn(tagList).when(tagsService).readAllTags();
        mockMvc.perform(MockMvcRequestBuilders.get("/tags"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(response));
    }

    @Test
    public void ShouldReturnJSONWhenGetTagByIdAndOKStatus() throws Exception {
        Tag actualTag=new Tag(1L,"Tag");
        String response = jsonMapper.writeValueAsString(actualTag);
        when(tagsService.readTag(1L)).thenReturn(actualTag);
        mockMvc.perform(MockMvcRequestBuilders.get("/tags/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(response));
    }
    @Test
    public void ShouldReturnJSONofTagAndOKStatusWhenPostNewTag() throws Exception {
        Tag newTag=new Tag(null,"Tag");
        Tag createdTag = new Tag(1L,"Tag");
        String response = jsonMapper.writeValueAsString(createdTag);
        when(tagsService.createTag(newTag)).thenReturn(createdTag);
        mockMvc.perform(MockMvcRequestBuilders.post("/tags")
                        .content(jsonMapper.writeValueAsString(newTag))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(response));
    }
    @Test
    public void ShouldReturnNoContentStatusWhenDeleteTag() throws Exception {
        long id=1L;
        when(tagsService.deleteTag(1L)).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.delete("/tags/"+id))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("true"));
    }
}
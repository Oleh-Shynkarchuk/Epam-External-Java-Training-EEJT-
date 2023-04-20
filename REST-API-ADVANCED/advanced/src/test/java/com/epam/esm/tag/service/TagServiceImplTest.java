package com.epam.esm.tag.service;

import com.epam.esm.tag.entity.Tag;
import com.epam.esm.tag.exception.TagInvalidRequestException;
import com.epam.esm.tag.exception.TagNotFoundException;
import com.epam.esm.tag.repo.TagRepository;
import com.epam.esm.tag.validation.TagValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @Mock
    private TagRepository tagRepository;
    @Mock
    private TagValidator validator;
    @InjectMocks
    private TagServiceImpl tagService;

    @Test
    void getAllTag() {
        long availableAmountOfOrders = 2L;
        Pageable pageable = PageRequest.of(0, 20);
        Tag tag1 = Tag.builder().id("1").build();
        Tag tag2 = Tag.builder().id("2").build();
        Page<Tag> expected = new PageImpl<>(List.of(tag1, tag2), pageable, availableAmountOfOrders);

        Mockito.when(tagRepository.count()).thenReturn(availableAmountOfOrders);
        Mockito.when(validator.validPageableRequest(availableAmountOfOrders, pageable)).thenReturn(pageable);
        Mockito.when(tagRepository.findAll(pageable)).thenReturn(expected);

        assertEquals(expected.toList(), tagService.getAllTags(pageable));
    }

    @Test
    void getAllTagShouldThrowItemNotFound() {
        long availableAmountOfOrders = 0L;
        Pageable pageable = PageRequest.of(0, 20);
        Page<Tag> expected = new PageImpl<>(List.of(), pageable, availableAmountOfOrders);

        Mockito.when(tagRepository.count()).thenReturn(availableAmountOfOrders);
        Mockito.when(validator.validPageableRequest(availableAmountOfOrders, pageable)).thenReturn(pageable);
        Mockito.when(tagRepository.findAll(pageable)).thenReturn(expected);

        assertThrows(TagNotFoundException.class, () -> tagService.getAllTags(pageable));
    }


    @Test
    void getTagById() {
        String requestId = "1";
        Tag expected = Tag.builder().id(requestId).build();

        Mockito.when(tagRepository.findById(Long.valueOf(requestId))).thenReturn(Optional.of(expected));

        assertEquals(expected, tagService.getTagById(requestId));
    }

    @Test
    void getTagByIdShouldThrowItemNotFound() {
        String requestId = "1";

        Mockito.when(tagRepository.findById(Long.valueOf(requestId))).thenReturn(Optional.empty());

        assertThrows(TagNotFoundException.class, () -> tagService.getTagById(requestId));
    }

    @Test
    void getOptionalTagByName() {
        String request = "testTagName";
        Tag expected = Tag.builder().id("1").name(request).build();

        Mockito.when(tagRepository.findByName(request)).thenReturn(Optional.of(expected));

        assertEquals(Optional.of(expected), tagService.getTagByName(request));
    }

    @Test
    void createTag() {
        String name = "testTag";
        Tag newTag = Tag.builder().id(null).name(name).build();
        Tag expected = Tag.builder().id("1").name(name).build();
        Mockito.when(tagRepository.existsByName(newTag.getName())).thenReturn(false);
        Mockito.when(tagRepository.save(newTag)).thenReturn(expected);

        assertEquals(expected, tagService.createTag(newTag));
    }

    @Test
    void createTagShouldThrowInvalidRequest() {
        String name = "testTag";
        Tag newTag = Tag.builder().id(null).name(name).build();
        Mockito.when(tagRepository.existsByName(newTag.getName())).thenReturn(true);

        assertThrows(TagInvalidRequestException.class, () -> tagService.createTag(newTag));
    }

    @Test
    void deleteTag() {
        String requestId = "1";

        Mockito.when(tagRepository.existsById(Long.valueOf(requestId))).thenReturn(true);
        Mockito.doNothing().when(tagRepository).deleteById(Long.valueOf(requestId));

        assertDoesNotThrow(() -> tagService.deleteTagById(requestId));
        Mockito.verify(tagRepository).deleteById(Long.valueOf(requestId));
    }

    @Test
    void deleteTagShouldThrowNotFound() {
        String requestId = "1";

        Mockito.when(tagRepository.existsById(Long.valueOf(requestId))).thenReturn(false);

        assertThrows(TagNotFoundException.class, () -> tagService.deleteTagById(requestId));
    }

    @Test
    void getMostWidelyUsedTag() {
        String idMostUsedTAG = "1";
        Tag expected = Tag.builder().id(idMostUsedTAG).name("TestTag").build();
        Mockito.when(tagRepository.findId()).thenReturn(Long.valueOf(idMostUsedTAG));
        Mockito.when(tagRepository.findById(Long.valueOf(idMostUsedTAG))).thenReturn(Optional.of(expected));
        assertEquals(expected, tagService.getMostWidelyUsedTag());
    }
}
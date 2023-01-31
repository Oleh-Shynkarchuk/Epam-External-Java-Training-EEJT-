package com.epam.esm.tag.service;

import com.epam.esm.tag.entity.Tag;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        Tag tag1 = Tag.builder().id(1L).build();
        Tag tag2 = Tag.builder().id(2L).build();
        Page<Tag> expected = new PageImpl<>(List.of(tag1, tag2), pageable, availableAmountOfOrders);

        Mockito.when(tagRepository.count()).thenReturn(availableAmountOfOrders);
        Mockito.when(validator.validPageableRequest(availableAmountOfOrders, pageable)).thenReturn(pageable);
        Mockito.when(tagRepository.findAll(pageable)).thenReturn(expected);

        assertEquals(expected.toList(), tagService.getAllTag(pageable));
    }

    @Test
    void getAllTagShouldThrowItemNotFound() {
        long availableAmountOfOrders = 0L;
        Pageable pageable = PageRequest.of(0, 20);
        Page<Tag> expected = new PageImpl<>(List.of(), pageable, availableAmountOfOrders);

        Mockito.when(tagRepository.count()).thenReturn(availableAmountOfOrders);
        Mockito.when(validator.validPageableRequest(availableAmountOfOrders, pageable)).thenReturn(pageable);
        Mockito.when(tagRepository.findAll(pageable)).thenReturn(expected);

        assertThrows(TagNotFoundException.class, () -> tagService.getAllTag(pageable));
    }


    @Test
    void getTagById() {
        long requestId = 1L;
        Tag expected = Tag.builder().id(requestId).build();

        Mockito.when(tagRepository.findById(requestId)).thenReturn(Optional.of(expected));

        assertEquals(expected, tagService.getTag(requestId));
    }

    @Test
    void getTagByIdShouldThrowItemNotFound() {
        long requestId = 1L;

        Mockito.when(tagRepository.findById(requestId)).thenReturn(Optional.empty());

        assertThrows(TagNotFoundException.class, () -> tagService.getTag(requestId));
    }

    @Test
    void getOptionalTagByName() {
        String request = "testTagName";
        Tag expected = Tag.builder().id(1L).name(request).build();

        Mockito.when(tagRepository.getTagByName(request)).thenReturn(Optional.of(expected));

        assertEquals(Optional.of(expected), tagService.getTagByName(request));
    }

    @Test
    void createTag() {
    }

    @Test
    void deleteTag() {
        long requestId = 1L;
        Tag expected = Tag.builder().id(requestId).build();

        Mockito.when(tagRepository.findById(requestId)).thenReturn(Optional.of(expected));

        assertEquals(expected, tagService.getTag(requestId));
    }

    @Test
    void getMostWidelyUsedTag() {
    }
}
package com.epam.esm.tags.service;

import com.epam.esm.integration.sqlrepo.MySQLRepository;
import com.epam.esm.tags.entity.Tag;
import com.epam.esm.tags.exception.TagInvalidRequestException;
import com.epam.esm.tags.exception.TagNotFoundException;
import com.epam.esm.tags.exception.TagNotRepresentException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class TagsServiceImplTest {
    @Mock
    private MySQLRepository tagRepository;
    @InjectMocks
    private TagsServiceImpl tagsService;

    @Test
    void readTagShouldThrowInvalidRequestCuzNegativeOrZeroID() {
        assertThrows(TagInvalidRequestException.class, () -> tagsService.readTag(0L));

        assertThrows(TagInvalidRequestException.class, () -> tagsService.readTag(-4L));
    }

    @Test
    void readTagShouldThrowNotFound() {
        long positiveId = 1L;
        Mockito.when(tagRepository.getTagById(positiveId)).thenReturn(Optional.empty());

        assertThrows(TagNotFoundException.class, () -> tagsService.readTag(positiveId));

    }

    @Test
    void readTagShouldReturnTagById() {
        long positiveId = 1L;
        Tag expected = new Tag(positiveId, "testName");
        Mockito.when(tagRepository.getTagById(positiveId)).thenReturn(Optional.of(expected));

        assertEquals(expected, tagsService.readTag(positiveId));
    }
    @Test
    void readAllTagsShouldThrowNotFound() {

        Mockito.when(tagRepository.getAllTags()).thenReturn(Optional.empty());

        assertThrows(TagNotFoundException.class, () -> tagsService.readAllTags());
    }

    @Test
    void readAllTags() {
        List<Tag> expectedTagList = List.of(new Tag(1L,"testTag1"),new Tag(2L,"testTag2"));
        Mockito.when(tagRepository.getAllTags()).thenReturn(Optional.of(expectedTagList));
        assertEquals(expectedTagList,tagsService.readAllTags());
    }

    @Test
    void createTagShouldThrowInvalidRequestIfNameNotUnique() {
        Tag newTagExist = new Tag(null,"nameExist");
        Mockito.when(tagRepository.tagByNameExist(newTagExist.getName())).thenReturn(true);
        assertThrows(TagInvalidRequestException.class, () -> tagsService.createTag(newTagExist));
    }
    @Test
    void createTagShouldThrowNotRepresent() {
        Tag newTag = new Tag(null,"nameExist");

        Mockito.when(tagRepository.tagByNameExist(newTag.getName())).thenReturn(false);
        Mockito.when(tagRepository.createNewTag(newTag)).thenReturn(Optional.empty());

        assertThrows(TagNotRepresentException.class, () -> tagsService.createTag(newTag));
    }
    @Test
    void createTagShouldReturnCreatedTag() {
        Tag newTag = new Tag(null,"nameExist");

        Mockito.when(tagRepository.tagByNameExist(newTag.getName())).thenReturn(false);
        Mockito.when(tagRepository.createNewTag(newTag)).thenReturn(Optional.of(new Tag(1L,"nameExist")));

        assertEquals(newTag.getName(),tagsService.createTag(newTag).getName());
    }

    @Test
    void deleteTagThrowInvalidRequestCuzNegativeOrZeroID() {
        assertThrows(TagInvalidRequestException.class, () -> tagsService.deleteTag(0L));

        assertThrows(TagInvalidRequestException.class, () -> tagsService.deleteTag(-4L));
    }
    @Test
    void deleteTagThrowTagNotFound() {
        long positiveId = 1L;
        Mockito.when(tagRepository.getTagById(positiveId)).thenReturn(Optional.empty());

        assertThrows(TagNotFoundException.class, () -> tagsService.deleteTag(positiveId));
    }
    @Test
    void deleteTagThrowTagNotRepresent() {
        long positiveId = 1L;
        Tag expected = new Tag(positiveId, "testName");
        Mockito.when(tagRepository.getTagById(positiveId)).thenReturn(Optional.of(expected));
        Mockito.when(tagRepository.deleteTagById(1L)).thenReturn(false);
        assertThrows(TagNotRepresentException.class, () -> tagsService.deleteTag(positiveId));
    }
    @Test
    void deleteTag() {
        long positiveId = 1L;
        Tag expected = new Tag(positiveId, "testName");
        Mockito.when(tagRepository.getTagById(positiveId)).thenReturn(Optional.of(expected));
        Mockito.when(tagRepository.deleteTagById(1L)).thenReturn(true);
        tagsService.deleteTag(positiveId);
        Mockito.verify(tagRepository).deleteTagById(positiveId);
    }
}
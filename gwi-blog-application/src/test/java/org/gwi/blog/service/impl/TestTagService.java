package org.gwi.blog.service.impl;

import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.gwi.blog.dto.TagDto;
import org.gwi.blog.entity.Tag;
import org.gwi.blog.exception.TagAlreadyExist;
import org.gwi.blog.exception.TagNotFound;
import org.gwi.blog.repository.TagRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TestTagService {

    private static final String TAG_FRESH = "fresh";

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagService tagService;

    @Captor
    private ArgumentCaptor<Tag> tagArgumentCaptor;

    @Test
    public void testGetAllTags() {
        Mockito.when(tagRepository.findAll()).thenReturn(List.of(new Tag(1, TAG_FRESH)));

        List<TagDto> actualTags = tagService.getAllTags();

        Assertions.assertThat(actualTags).isEqualTo(List.of(new TagDto(1, TAG_FRESH)));
    }

    @Test(expected = TagAlreadyExist.class)
    public void testCreateTagThrowTagAlreadyExistExceptionWhenTagWithSameNameExist() {
        Mockito.when(tagRepository.findByName(TAG_FRESH)).thenReturn(
            Optional.of(new Tag(1, TAG_FRESH)));

        tagService.createTag(TAG_FRESH);
    }

    @Test
    public void testCreateTag() {
        Mockito.when(tagRepository.findByName(TAG_FRESH)).thenReturn(Optional.empty());
        Mockito.when(tagRepository.save(Mockito.any())).thenReturn(new Tag(1, TAG_FRESH));
        TagDto actualTag = tagService.createTag(TAG_FRESH);

        Mockito.verify(tagRepository, Mockito.times(1)).save(tagArgumentCaptor.capture());

        Assertions.assertThat(tagArgumentCaptor.getValue().getName()).isEqualTo(TAG_FRESH);
        Assertions.assertThat(actualTag).isEqualTo(new TagDto(1, TAG_FRESH));
    }

    @Test(expected = TagNotFound.class)
    public void testRenameTagThrowTagNotFoundExceptionWhenTagNotExist() {
        Mockito.when(tagRepository.findById(1)).thenReturn(Optional.empty());
        Mockito.when(tagRepository.findByName(TAG_FRESH)).thenReturn(Optional.empty());

        tagService.renameTag(1, TAG_FRESH);
    }

    @Test(expected = TagAlreadyExist.class)
    public void testRenameTagThrowTagAlreadyExistExceptionWhenTagWithSameNameExist() {
        Mockito.when(tagRepository.findByName(TAG_FRESH))
            .thenReturn(Optional.of(new Tag(2, TAG_FRESH)));

        tagService.renameTag(1, TAG_FRESH);
    }

    @Test
    public void testRenameTag() {
        Mockito.when(tagRepository.findById(1)).thenReturn(Optional.of(new Tag(1, "toRename")));
        Mockito.when(tagRepository.findByName(TAG_FRESH)).thenReturn(Optional.empty());
        Mockito.when(tagRepository.save(Mockito.any())).thenReturn(new Tag(1, TAG_FRESH));

        TagDto actualTag = tagService.renameTag(1, TAG_FRESH);

        Assertions.assertThat(actualTag).isEqualTo(new TagDto(1, TAG_FRESH));
        Mockito.verify(tagRepository, Mockito.times(1)).save(tagArgumentCaptor.capture());
        Assertions.assertThat(tagArgumentCaptor.getValue().getId()).isEqualTo(1);
        Assertions.assertThat(tagArgumentCaptor.getValue().getName()).isEqualTo(TAG_FRESH);
    }

    @Test(expected = TagNotFound.class)
    public void testRemoveTagThrowTagNotFoundExceptionWhenTagNotExist() {
        Mockito.when(tagRepository.findById(1)).thenReturn(Optional.empty());

        tagService.deleteTag(1);
    }

    @Test
    public void testRemoveTagWhenTagExist() {
        Tag tagToRemove = new Tag(1, "testTag");
        Mockito.when(tagRepository.findById(1)).thenReturn(Optional.of(tagToRemove));

        TagDto actualTag = tagService.deleteTag(1);

        Mockito.verify(tagRepository, Mockito.times(1)).delete(tagToRemove);
        Assertions.assertThat(actualTag).isEqualTo(new TagDto(1, "testTag"));
    }

}

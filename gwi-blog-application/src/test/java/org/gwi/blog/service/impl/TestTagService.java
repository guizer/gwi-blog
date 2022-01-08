package org.gwi.blog.service.impl;

import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.gwi.blog.dto.TagDto;
import org.gwi.blog.entity.Tag;
import org.gwi.blog.exception.TagNameAlreadyExist;
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

    private static final String FRESH_TAG_NAME = "Fresh";
    private static final String FRESH_TAG_SLUG = "fresh-slug";
    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagService tagService;

    @Captor
    private ArgumentCaptor<Tag> tagArgumentCaptor;

    @Test
    public void testGetAllTags() {
        Mockito.when(tagRepository.findAll())
            .thenReturn(List.of(new Tag(1, FRESH_TAG_NAME, FRESH_TAG_SLUG)));

        List<TagDto> actualTags = tagService.getAllTags();

        Assertions.assertThat(actualTags)
            .isEqualTo(List.of(new TagDto(1, FRESH_TAG_NAME, FRESH_TAG_SLUG)));
    }

    @Test(expected = TagNameAlreadyExist.class)
    public void testCreateTagThrowTagAlreadyExistExceptionWhenTagWithSameNameExist() {
        Mockito.when(tagRepository.findByName(FRESH_TAG_NAME)).thenReturn(
            Optional.of(new Tag(1, FRESH_TAG_NAME, FRESH_TAG_SLUG)));

        tagService.createTag(FRESH_TAG_NAME);
    }

    @Test
    public void testCreateTag() {
        Mockito.when(tagRepository.findByName(FRESH_TAG_NAME)).thenReturn(Optional.empty());
        Mockito.when(tagRepository.save(Mockito.any()))
            .thenReturn(new Tag(1, FRESH_TAG_NAME, FRESH_TAG_SLUG));
        TagDto actualTag = tagService.createTag(FRESH_TAG_NAME);

        Mockito.verify(tagRepository, Mockito.times(1)).save(tagArgumentCaptor.capture());

        Assertions.assertThat(tagArgumentCaptor.getValue().getName()).isEqualTo(FRESH_TAG_NAME);
        Assertions.assertThat(actualTag).isEqualTo(new TagDto(1, FRESH_TAG_NAME, FRESH_TAG_SLUG));
    }

    @Test(expected = TagNotFound.class)
    public void testUpdateTagThrowTagNotFoundExceptionWhenTagNotExist() {
        Mockito.when(tagRepository.findById(1)).thenReturn(Optional.empty());
        Mockito.when(tagRepository.findByName(FRESH_TAG_NAME)).thenReturn(Optional.empty());
        Mockito.when(tagRepository.findBySlug(FRESH_TAG_SLUG)).thenReturn(Optional.empty());

        tagService.updateTag(1, FRESH_TAG_NAME, FRESH_TAG_SLUG);
    }

    @Test(expected = TagNameAlreadyExist.class)
    public void testUpdateTagThrowTagAlreadyExistExceptionWhenTagWithSameNameExist() {
        Mockito.when(tagRepository.findByName(FRESH_TAG_NAME))
            .thenReturn(Optional.of(new Tag(2, FRESH_TAG_NAME, "oldSlug")));
        Mockito.when(tagRepository.findBySlug(FRESH_TAG_SLUG)).thenReturn(Optional.empty());

        tagService.updateTag(1, FRESH_TAG_NAME, FRESH_TAG_SLUG);
    }

    @Test(expected = TagNameAlreadyExist.class)
    public void testUpdateTagThrowTagAlreadyExistExceptionWhenTagWithSameSlugExist() {
        Mockito.when(tagRepository.findByName(FRESH_TAG_SLUG)).thenReturn(Optional.empty());
        Mockito.when(tagRepository.findBySlug(FRESH_TAG_SLUG))
            .thenReturn(Optional.of(new Tag(2, "oldName", FRESH_TAG_SLUG)));

        tagService.updateTag(1, FRESH_TAG_NAME, FRESH_TAG_SLUG);
    }

    @Test
    public void testUpdateTag() {
        Mockito.when(tagRepository.findById(1))
            .thenReturn(Optional.of(new Tag(1, "oldName", "oldSlug")));
        Mockito.when(tagRepository.findByName(FRESH_TAG_NAME)).thenReturn(Optional.empty());
        Mockito.when(tagRepository.findByName(FRESH_TAG_SLUG)).thenReturn(Optional.empty());
        Mockito.when(tagRepository.save(Mockito.any()))
            .thenReturn(new Tag(1, FRESH_TAG_NAME, FRESH_TAG_SLUG));

        TagDto actualTag = tagService.updateTag(1, FRESH_TAG_NAME, FRESH_TAG_SLUG);

        Assertions.assertThat(actualTag).isEqualTo(new TagDto(1, FRESH_TAG_NAME, FRESH_TAG_SLUG));
        Mockito.verify(tagRepository, Mockito.times(1)).save(tagArgumentCaptor.capture());
        Assertions.assertThat(tagArgumentCaptor.getValue().getId()).isEqualTo(1);
        Assertions.assertThat(tagArgumentCaptor.getValue().getName()).isEqualTo(FRESH_TAG_NAME);
    }

    @Test(expected = TagNotFound.class)
    public void testRemoveTagThrowTagNotFoundExceptionWhenTagNotExist() {
        Mockito.when(tagRepository.findById(1)).thenReturn(Optional.empty());

        tagService.deleteTag(1);
    }

    @Test
    public void testRemoveTagWhenTagExist() {
        Tag tagToRemove = new Tag(1, "oldName", "oldSlug");
        Mockito.when(tagRepository.findById(1)).thenReturn(Optional.of(tagToRemove));

        TagDto actualTag = tagService.deleteTag(1);

        Mockito.verify(tagRepository, Mockito.times(1)).delete(tagToRemove);
        Assertions.assertThat(actualTag).isEqualTo(new TagDto(1, "oldName", "oldSlug"));
    }

}

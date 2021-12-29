package org.gwi.blog.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.gwi.blog.dto.CommentDto;
import org.gwi.blog.entity.Comment;
import org.gwi.blog.exception.CommentNotFound;
import org.gwi.blog.repository.CommentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TestCommentService {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    @Captor
    private ArgumentCaptor<Comment> commentArgumentCaptor;

    @Test(expected = CommentNotFound.class)
    public void testRemoveCommentThrowCommentNotFoundExceptionWhenCommentNotExist() {
        Mockito.when(commentRepository.findById(1)).thenReturn(Optional.empty());

        commentService.deleteComment(1);
    }

    @Test
    public void testRemoveCommentWhenCommentExist() {
        Comment commentToRemove = Comment.builder()
            .id(1)
            .content("content")
            .creationDate(LocalDateTime.now())
            .lastModificationDate(LocalDateTime.now())
            .build();
        Mockito.when(commentRepository.findById(1)).thenReturn(Optional.of(commentToRemove));

        CommentDto actualComment = commentService.deleteComment(1);

        Mockito.verify(commentRepository, Mockito.times(1)).delete(commentToRemove);
        Assertions.assertThat(actualComment).isEqualTo(commentToRemove.convertToDto());
    }

    @Test(expected = CommentNotFound.class)
    public void testUpdateCommentThrowCommentNotFoundExceptionWhenCommentNotExist() {
        Mockito.when(commentRepository.findById(1)).thenReturn(Optional.empty());

        commentService.updateComment(1, "new content");
    }

    @Test
    public void testUpdateCommentWhenCommentExist() {
        LocalDateTime creationDate = LocalDateTime.now();
        Comment commentToUpdate = Comment.builder()
            .id(1)
            .content("old content")
            .creationDate(creationDate)
            .lastModificationDate(creationDate)
            .build();
        Mockito.when(commentRepository.findById(1)).thenReturn(Optional.of(commentToUpdate));
        LocalDateTime lastModificationDate = LocalDateTime.now();
        Comment expectedUpdatedComment = Comment.builder()
            .id(1)
            .content("new content")
            .creationDate(creationDate)
            .lastModificationDate(lastModificationDate)
            .build();
        Mockito.when(commentRepository.save(Mockito.any())).thenReturn(expectedUpdatedComment);
        CommentDto updatedCommentDto = commentService.updateComment(1, "new content");


        Mockito.verify(commentRepository, Mockito.times(1)).save(commentArgumentCaptor.capture());
        Assertions.assertThat(commentArgumentCaptor.getValue().getContent())
            .isEqualTo("new content");
        Assertions.assertThat(updatedCommentDto)
            .extracting(CommentDto::getId, CommentDto::getContent)
            .containsExactly(1, "new content");
    }


}

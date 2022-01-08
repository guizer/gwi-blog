package org.gwi.blog.controller;

import com.google.gson.Gson;
import org.gwi.blog.TestConfiguration;
import org.gwi.blog.controller.request.CategoryCreationRequest;
import org.gwi.blog.controller.request.CommentCreationRequest;
import org.gwi.blog.dto.CommentDto;
import org.gwi.blog.exception.CommentNotFound;
import org.gwi.blog.service.ICommentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringJUnit4ClassRunner.class)
@Import(TestConfiguration.class)
@WebMvcTest(controllers = CommentRestController.class)
@AutoConfigureMockMvc
public class TestCommentRestController {

    private static final String COMMENT_CONTENT = "hello!";

    @Autowired
    private Gson gson;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ICommentService commentService;

    @Test
    public void testUpdateCommentRespondWith200WhenCommentExist() throws Exception {
        CommentDto expectedComment =
            CommentDto.builder().id(1).content(COMMENT_CONTENT).author("luffy")
                .creationDate("2020-12-01").lastModificationDate("2020-12-02").build();
        Mockito.when(commentService.updateComment(1, COMMENT_CONTENT)).thenReturn(expectedComment);
        mockMvc.perform(MockMvcRequestBuilders.put(CommentRestController.NAMESPACE + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(new CommentCreationRequest(COMMENT_CONTENT))))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(gson.toJson(expectedComment)));
    }

    @Test
    public void testUpdateCommentRespondWith404WhenCommentNotExist() throws Exception {
        Mockito.when(commentService.updateComment(1, COMMENT_CONTENT))
            .thenThrow(CommentNotFound.class);
        mockMvc.perform(MockMvcRequestBuilders.put(CommentRestController.NAMESPACE + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(new CommentCreationRequest(COMMENT_CONTENT))))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testDeleteCommentRespondWith200WhenCommentExist() throws Exception {
        CommentDto expectedComment =
            CommentDto.builder().id(1).content(COMMENT_CONTENT).author("luffy")
                .creationDate("2020-12-01").lastModificationDate("2020-12-02").build();
        Mockito.when(commentService.deleteComment(1)).thenReturn(expectedComment);
        mockMvc.perform(MockMvcRequestBuilders.delete(CommentRestController.NAMESPACE + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(new CommentCreationRequest(COMMENT_CONTENT))))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(gson.toJson(expectedComment)));
    }

    @Test
    public void testDeleteCommentRespondWith404WhenCommentNotExist() throws Exception {
        Mockito.when(commentService.deleteComment(1)).thenThrow(CommentNotFound.class);
        mockMvc.perform(MockMvcRequestBuilders.delete(CommentRestController.NAMESPACE + "/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}

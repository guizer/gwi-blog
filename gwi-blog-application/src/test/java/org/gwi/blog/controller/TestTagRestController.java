package org.gwi.blog.controller;

import com.google.gson.Gson;
import java.util.List;
import org.gwi.blog.TestConfiguration;
import org.gwi.blog.controller.request.CategoryCreationRequest;
import org.gwi.blog.dto.TagDto;
import org.gwi.blog.exception.TagNameAlreadyExist;
import org.gwi.blog.exception.TagNotFound;
import org.gwi.blog.exception.TagSlugAlreadyExist;
import org.gwi.blog.service.ITagService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringJUnit4ClassRunner.class)
@Import(TestConfiguration.class)
@WebMvcTest(controllers = TagRestController.class)
@AutoConfigureMockMvc
public class TestTagRestController {

    private static final String FRESH_TAG_NAME = "Fresh";
    private static final String FRESH_TAG_SLUG = "fresh";

    @Autowired
    private Gson gson;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ITagService tagService;

    @Test
    public void testGetAllTagsRespondWith200() throws Exception {
        List<TagDto> tags = List.of(new TagDto(1, FRESH_TAG_NAME, FRESH_TAG_SLUG));
        Mockito.when(tagService.getAllTags()).thenReturn(tags);
        mockMvc.perform(MockMvcRequestBuilders.get(TagRestController.NAMESPACE)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(gson.toJson(tags)));
    }

    @Test
    public void testCreateTagRespondWith201WhenTagNotExist() throws Exception {
        TagDto expectedTag = new TagDto(1, FRESH_TAG_NAME, FRESH_TAG_SLUG);
        Mockito.when(tagService.createTag(FRESH_TAG_NAME)).thenReturn(expectedTag);
        mockMvc.perform(MockMvcRequestBuilders.post(TagRestController.NAMESPACE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(new CategoryCreationRequest(FRESH_TAG_NAME, FRESH_TAG_SLUG))))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.content().json(gson.toJson(expectedTag)));
    }

    @Test
    public void testCreateTagRespondWith400WhenTagExist() throws Exception {
        Mockito.when(tagService.createTag(FRESH_TAG_NAME))
            .thenThrow(TagNameAlreadyExist.class);
        mockMvc.perform(MockMvcRequestBuilders.post(TagRestController.NAMESPACE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(new CategoryCreationRequest(FRESH_TAG_NAME, FRESH_TAG_SLUG))))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testUpdateTagRespondWith200WhenTagExist() throws Exception {
        TagDto expectedTag = new TagDto(1, FRESH_TAG_NAME, FRESH_TAG_SLUG);
        Mockito.when(tagService.updateTag(1, FRESH_TAG_NAME, FRESH_TAG_SLUG))
            .thenReturn(expectedTag);
        mockMvc.perform(MockMvcRequestBuilders.put(TagRestController.NAMESPACE + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(new CategoryCreationRequest(FRESH_TAG_NAME, FRESH_TAG_SLUG))))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(gson.toJson(expectedTag)));
    }

    @Test
    public void testUpdateTagRespondWith404WhenTagNotExist() throws Exception {
        Mockito.when(tagService.updateTag(1, FRESH_TAG_NAME, FRESH_TAG_SLUG))
            .thenThrow(TagNotFound.class);
        mockMvc.perform(MockMvcRequestBuilders.put(TagRestController.NAMESPACE + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(new CategoryCreationRequest(FRESH_TAG_NAME, FRESH_TAG_SLUG))))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testUpdateTagRespondWith400WhenNewNameAlreadyExist() throws Exception {
        Mockito.when(tagService.updateTag(1, FRESH_TAG_NAME, FRESH_TAG_SLUG))
            .thenThrow(TagNameAlreadyExist.class);
        mockMvc.perform(MockMvcRequestBuilders.put(TagRestController.NAMESPACE + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(new CategoryCreationRequest(FRESH_TAG_NAME, FRESH_TAG_SLUG))))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testUpdateTagRespondWith400WhenNewSlugAlreadyExist() throws Exception {
        Mockito.when(tagService.updateTag(1, FRESH_TAG_NAME, FRESH_TAG_SLUG))
            .thenThrow(TagSlugAlreadyExist.class);
        mockMvc.perform(MockMvcRequestBuilders.put(TagRestController.NAMESPACE + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(new CategoryCreationRequest(FRESH_TAG_NAME, FRESH_TAG_SLUG))))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testDeleteTagRespondWith200WhenTagExist() throws Exception {
        TagDto expectedTag = new TagDto(1, FRESH_TAG_NAME, FRESH_TAG_SLUG);
        Mockito.when(tagService.deleteTag(1)).thenReturn(expectedTag);
        mockMvc.perform(MockMvcRequestBuilders.delete(TagRestController.NAMESPACE + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(new CategoryCreationRequest(FRESH_TAG_NAME, FRESH_TAG_SLUG))))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(gson.toJson(expectedTag)));
    }

    @Test
    public void testDeleteTagRespondWith404WhenTagNotExist() throws Exception {
        Mockito.when(tagService.deleteTag(1)).thenThrow(TagNotFound.class);
        mockMvc.perform(MockMvcRequestBuilders.delete(TagRestController.NAMESPACE + "/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}

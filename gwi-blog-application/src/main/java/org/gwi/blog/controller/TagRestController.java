package org.gwi.blog.controller;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.gwi.blog.controller.request.CategoryCreationRequest;
import org.gwi.blog.dto.TagDto;
import org.gwi.blog.exception.TagNameAlreadyExist;
import org.gwi.blog.exception.TagNotFound;
import org.gwi.blog.exception.TagSlugAlreadyExist;
import org.gwi.blog.service.ITagService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping(value = TagRestController.NAMESPACE)
@RestController
public class TagRestController {

    static final String NAMESPACE = "/api/v1/tags";

    private final ITagService tagService;

    public TagRestController(ITagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public List<TagDto> getAllTags() {
        log.info("[GWI-BLOG] Retrieve all tags");
        return tagService.getAllTags();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public TagDto createTag(@RequestBody CategoryCreationRequest createRequest) {
        log.info("[GWI-BLOG] Create tag for name {}", createRequest.getName());
        return tagService.createTag(createRequest.getName());
    }

    @PutMapping("/{tagId}")
    public TagDto renameTag(@PathVariable int tagId,
                            @RequestBody CategoryCreationRequest updateRequest) {
        log.info("[GWI-BLOG] Rename tag of id {} to {}", tagId, updateRequest.getName());
        return tagService.updateTag(tagId, updateRequest.getName(), updateRequest.getSlug());
    }

    @DeleteMapping("/{tagId}")
    public TagDto deleteTag(@PathVariable int tagId) {
        log.info("[GWI-BLOG] Delete tag of id {}", tagId);
        return tagService.deleteTag(tagId);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(TagNotFound.class)
    public String handleException(TagNotFound exception) {
        return exception.getMessage();
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TagNameAlreadyExist.class)
    public String handleException(TagNameAlreadyExist exception) {
        return exception.getMessage();
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TagSlugAlreadyExist.class)
    public String handleException(TagSlugAlreadyExist exception) {
        return exception.getMessage();
    }

}

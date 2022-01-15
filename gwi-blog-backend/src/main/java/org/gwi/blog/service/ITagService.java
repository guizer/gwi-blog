package org.gwi.blog.service;

import java.util.List;
import org.gwi.blog.exception.TagNotFound;
import org.gwi.blog.dto.TagDto;

public interface ITagService {

    /**
     * Retrieve all categories.
     */
    List<TagDto> getAllTags();

    /**
     * Delete the tag with the given id.
     *
     * @param tagId the tag id
     * @return the deleted tag
     * @throws TagNotFound if the tag does not exist
     */
    TagDto deleteTag(int tagId);

    /**
     * Create an tag.
     */
    TagDto createTag(String name, String slug);

    /**
     * Rename the tag with the given id.
     *
     * @param tagId   the tag id
     * @param newName the new tag name
     * @param newSlug the new tag slug
     * @return the updated tag
     * @throws TagNotFound if the tag does not exist
     */
    TagDto updateTag(int tagId, String newName, String newSlug);

}

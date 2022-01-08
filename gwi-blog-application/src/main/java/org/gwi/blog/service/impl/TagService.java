package org.gwi.blog.service.impl;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.gwi.blog.dto.TagDto;
import org.gwi.blog.entity.Tag;
import org.gwi.blog.exception.TagNameAlreadyExist;
import org.gwi.blog.exception.TagNotFound;
import org.gwi.blog.exception.TagSlugAlreadyExist;
import org.gwi.blog.repository.TagRepository;
import org.gwi.blog.service.ITagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class TagService implements ITagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<TagDto> getAllTags() {
        return tagRepository.findAll().stream().map(Tag::convertToDto).toList();
    }

    @Transactional
    @Override
    public TagDto createTag(String name, String slug) {
        checkTagExist(name, slug);
        Tag tagToCreate = new Tag();
        tagToCreate.setName(name);
        tagToCreate.setSlug(slug);
        return tagRepository.save(tagToCreate).convertToDto();
    }

    @Transactional
    @Override
    public TagDto updateTag(int tagId, String newName, String newSlug) {
        checkTagExist(newName, newSlug);
        Tag tagToRename = tagRepository.findById(tagId).orElseThrow(() -> new TagNotFound(tagId));
        tagToRename.setName(newName);
        return tagRepository.save(tagToRename).convertToDto();
    }

    @Transactional
    @Override
    public TagDto deleteTag(int tagId) {
        Tag tagToDelete = tagRepository.findById(tagId).orElseThrow(() -> new TagNotFound(tagId));
        tagRepository.delete(tagToDelete);
        return tagToDelete.convertToDto();
    }

    private void checkTagExist(String name, String slug) {
        tagRepository.findByName(name).ifPresent(tagFound -> {
            throw new TagNameAlreadyExist(name);
        });
        tagRepository.findBySlug(slug).ifPresent(tagFound -> {
            throw new TagSlugAlreadyExist(slug);
        });
    }

}

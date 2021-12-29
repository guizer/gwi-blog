package org.gwi.blog;

import org.gwi.blog.repository.ArticleRepository;
import org.gwi.blog.repository.CategoryRepository;
import org.gwi.blog.repository.CommentRepository;
import org.gwi.blog.repository.TagRepository;
import org.gwi.blog.service.IArticleService;
import org.gwi.blog.service.ICategoryService;
import org.gwi.blog.service.ICommentService;
import org.gwi.blog.service.ITagService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfiguration {

    @MockBean
    private ICategoryService categoryService;

    @MockBean
    private IArticleService articleService;

    @MockBean
    private ITagService tagService;

    @MockBean
    private ICommentService commentService;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private ArticleRepository articleRepository;

    @MockBean
    private TagRepository tagRepository;

    @MockBean
    private CommentRepository commentRepository;

}

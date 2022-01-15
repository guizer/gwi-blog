package org.gwi.blog.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.gwi.blog.entity.Article;
import org.gwi.blog.entity.Category;
import org.gwi.blog.repository.ArticleRepository;
import org.gwi.blog.repository.CategoryRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class StartupApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Set<String> SPORT_CATEGORIES = Set.of(
        "Basketball",
        "Boxing",
        "Football",
        "Handball",
        "Rugby",
        "Tennis",
        "Volleyball"
    );

    private final CategoryRepository categoryRepository;
    private final ArticleRepository articleRepository;
    private final Gson gson;

    public StartupApplicationListener(CategoryRepository categoryRepository,
                                      ArticleRepository articleRepository,
                                      Gson gson) {
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
        this.gson = gson;
    }

    @Transactional
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        for (String categoryName : SPORT_CATEGORIES) {
            Category createdCategory =
                categoryRepository.save(new Category(categoryName, categoryName.toLowerCase()));
            try (InputStream inputStream =
                     getClass().getResourceAsStream(
                         "/sport-news-samples/" + categoryName.toLowerCase() + ".json")) {
                if (inputStream != null) {
                    Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                    List<ArticleCreation> creations =
                        gson.fromJson(reader, new TypeToken<List<ArticleCreation>>() {
                        }.getType());
                    for (ArticleCreation creation : creations) {
                        LocalDateTime publishedAt =
                            LocalDateTime.parse(creation.publishedAt);
                        articleRepository.save(Article.builder()
                            .title(creation.getTitle())
                            .content(creation.getContent())
                            .category(createdCategory)
                            .imageUrl(creation.getImageUrl())
                            .author(creation.author)
                            .title(creation.getTitle())
                            .lastModifiedAt(publishedAt)
                            .publishedAt(publishedAt)
                            .build());
                    }
                }
            } catch (IOException exception) {
                log.error("The creation of news failed", exception);
            }
        }
    }

    @Data
    public static class ArticleCreation {

        private String title;
        private String content;
        private String imageUrl;
        private String author;
        private String publishedAt;

    }

}


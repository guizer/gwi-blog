package org.gwi.blog.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.gwi.blog.dto.CategoryDto;
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

    private final ICategoryService categoryService;
    private final IArticleService articleService;
    private final Gson gson;

    public StartupApplicationListener(ICategoryService categoryService,
                                      IArticleService articleService,
                                      Gson gson) {
        this.articleService = articleService;
        this.categoryService = categoryService;
        this.gson = gson;
    }

    @Transactional
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        for (String categoryName : SPORT_CATEGORIES) {
            CategoryDto createdCategory = categoryService.createCategory(categoryName);
            try (InputStream inputStream =
                     getClass().getResourceAsStream(
                         "/sport-news-samples/" + categoryName.toLowerCase() + ".json")) {
                if (inputStream != null) {
                    Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                    List<ArticleCreationRequest> requests =
                        gson.fromJson(reader, new TypeToken<List<ArticleCreationRequest>>() {
                        }.getType());
                    for (ArticleCreationRequest request : requests) {
                        request.setCategoryId(createdCategory.getId());
                        articleService.createArticle(request);
                    }
                }
            } catch (IOException exception) {
                log.error("The creation of news failed", exception);
            }
        }
    }

}


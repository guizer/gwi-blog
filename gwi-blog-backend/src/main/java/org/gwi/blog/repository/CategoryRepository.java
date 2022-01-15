package org.gwi.blog.repository;

import java.util.Optional;
import org.gwi.blog.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Optional<Category> findByName(String name);

    Optional<Category> findBySlug(String slug);

}

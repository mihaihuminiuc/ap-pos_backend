package com.wordpress.pos.demo.repository;

import com.wordpress.pos.demo.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Article findByArticleUUID(String uuid);
    Article findArticleByUssrId(Long id);
}

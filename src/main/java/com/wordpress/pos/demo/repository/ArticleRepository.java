package com.wordpress.pos.demo.repository;

import com.wordpress.pos.demo.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Article findByArticleUUID(String uuid);
    Article findArticleByUserId(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Article a SET a.articleTitle = :articleTitle, a.articleContent = :articleContent WHERE a.articleUUID = :articleUUID")
    void updateArticle(
            @Param("articleContent") String articleContent,
            @Param("articleTitle") String articleTitle,
            @Param("articleUUID") String articleUUID
    );

    @Modifying
    @Transactional
    @Query("DELETE FROM Article WHERE articleUUID = :articleUUID")
    void deleteArticle(
            @Param("articleUUID") String articleUUID
    );
}

package com.wordpress.pos.demo.repository;

import com.wordpress.pos.demo.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findAllBy(long id);
    Article findById(long id);
}

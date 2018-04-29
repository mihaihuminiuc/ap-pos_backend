package com.wordpress.pos.demo.service;

import com.wordpress.pos.demo.dto.ArticleDTO;
import com.wordpress.pos.demo.model.Article;
import com.wordpress.pos.demo.model.User;

import java.util.List;

public interface ArticleService {

    public List<Article> getAllArticles();
    public Article getArticleById(long id);
    public void saveArticle(User user, ArticleDTO articleDTO);
    public void updateArticle(User user, ArticleDTO articleDTO);
}

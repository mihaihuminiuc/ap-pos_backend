package com.wordpress.pos.demo.service;

import com.wordpress.pos.demo.dto.article.ArticleDTO;
import com.wordpress.pos.demo.dto.article.CompactArticleDTO;
import com.wordpress.pos.demo.model.Article;
import com.wordpress.pos.demo.model.User;

import java.util.List;

public interface ArticleService {

    public List<CompactArticleDTO> getAllArticles(int count);
    public ArticleDTO getArticleDTOByUUID(String uuid);
    public Article getArticleByUUID(String uuid);
    public void createArticle(User user, ArticleDTO articleDTO);
    public void updateArticle(User user, ArticleDTO articleDTO);
}

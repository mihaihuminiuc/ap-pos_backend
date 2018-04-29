package com.wordpress.pos.demo.service.impelemtation;

import com.wordpress.pos.demo.dto.ArticleDTO;
import com.wordpress.pos.demo.model.Article;
import com.wordpress.pos.demo.model.User;
import com.wordpress.pos.demo.repository.ArticleRepository;
import com.wordpress.pos.demo.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository){
        this.articleRepository = articleRepository;
    }

    @Override
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    @Override
    public Article getArticleById(long id) {
        return articleRepository.findById(id);
    }

    @Override
    public void saveArticle(User user, ArticleDTO articleDTO) {

        Article article = new Article();
        article.setUser(user);
        article.setArticleContent(articleDTO.getArticleDTO().getArticleContent());
        article.setArticleTitle(articleDTO.getArticleDTO().getArticleTitle());

        articleRepository.save(article);
    }

    @Override
    public void updateArticle(User user, ArticleDTO articleDTO) {

        Article article = new Article();
        article.setUser(user);
        article.setArticleContent(articleDTO.getArticleDTO().getArticleContent());

        articleRepository.save(article);
    }
}

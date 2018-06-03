package com.wordpress.pos.demo.service.implemtation;

import com.wordpress.pos.demo.dto.article.ArticleDTO;
import com.wordpress.pos.demo.dto.article.CompactArticleDTO;
import com.wordpress.pos.demo.model.Article;
import com.wordpress.pos.demo.model.User;
import com.wordpress.pos.demo.repository.ArticleRepository;
import com.wordpress.pos.demo.service.ArticleService;
import com.wordpress.pos.demo.util.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository){
        this.articleRepository = articleRepository;
    }

    @Override
    public List<CompactArticleDTO> getAllArticles(int count) {

        if(count>0){
            Pageable pageable = new PageRequest(count,20);
            Page<Article> articlePage = articleRepository.findAll(pageable);

            return ObjectMapperUtils.mapAll(articlePage.getContent(), CompactArticleDTO.class);
        }else{
            return ObjectMapperUtils.mapAll(articleRepository.findAll(), CompactArticleDTO.class);
        }
    }

    @Override
    public ArticleDTO getArticleDTOByUUID(String uuid) {
        Article article = articleRepository.findByArticleUUID(uuid);
        return ObjectMapperUtils.map(article, ArticleDTO.class);
    }

    @Override
    public Article getArticleByUUID(String uuid) {
        return ObjectMapperUtils.map(articleRepository.findByArticleUUID(uuid), Article.class);
    }

    @Override
    public Article getArticleByUserId(long id) {
        return ObjectMapperUtils.map(articleRepository.findArticleByUserId(id), Article.class);
    }

    @Override
    public void createArticle(User user, ArticleDTO articleDTO) {

        Article article = new Article();
        article.setUser(user);
        article.setArticleContent(articleDTO.getArticleContent());
        article.setArticleTitle(articleDTO.getArticleTitle());
        article.setArticleUUID(UUID.randomUUID().toString());

        articleRepository.save(article);
    }

    @Override
    public void updateArticle(ArticleDTO articleDTO) {
        articleRepository.updateArticle(articleDTO.getArticleContent(),articleDTO.getArticleTitle(),articleDTO.getArticleUUID());
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteArticle(String uuid) {
        long articleId = articleRepository.findByArticleUUID(uuid).getId();
        articleRepository.delete(articleId);
    }
}

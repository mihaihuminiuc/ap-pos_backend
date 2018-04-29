package com.wordpress.pos.demo.service;

import com.wordpress.pos.demo.dto.CommentDTO;
import com.wordpress.pos.demo.model.Article;
import com.wordpress.pos.demo.model.Comments;

import java.util.List;

public interface CommentService {

    public List<Comments> getAllCommentsForArticleId(long id);
    public void saveComment(CommentDTO commentDTO, Article article);
    public void updateArticle(CommentDTO commentDTO);
}

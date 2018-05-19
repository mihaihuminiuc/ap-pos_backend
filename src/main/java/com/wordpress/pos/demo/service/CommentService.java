package com.wordpress.pos.demo.service;

import com.wordpress.pos.demo.dto.CommentDTO;
import com.wordpress.pos.demo.model.Article;
import com.wordpress.pos.demo.model.Comments;
import com.wordpress.pos.demo.model.User;

import java.util.List;

public interface CommentService {

    public List<Comments> getAllCommentsForArticleId(String uuid);
    public void createComment(CommentDTO commentDTO, User user, Article article);
    public Comments getArticleByUserId(long id);
    public void updateArticle(CommentDTO commentDTO);
}

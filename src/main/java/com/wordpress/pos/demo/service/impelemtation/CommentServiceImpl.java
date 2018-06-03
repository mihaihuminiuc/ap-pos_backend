package com.wordpress.pos.demo.service.impelemtation;

import com.wordpress.pos.demo.dto.article.ArticleDTO;
import com.wordpress.pos.demo.dto.CommentDTO;
import com.wordpress.pos.demo.dto.article.CompactArticleDTO;
import com.wordpress.pos.demo.model.Article;
import com.wordpress.pos.demo.model.Comments;
import com.wordpress.pos.demo.model.User;
import com.wordpress.pos.demo.repository.CommentRepository;
import com.wordpress.pos.demo.service.CommentService;
import com.wordpress.pos.demo.util.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository){
        this.commentRepository = commentRepository;
    }

    @Override
    public List<CommentDTO> getAllCommentsForArticleId(String uuid) {
        List<CommentDTO> commentDTOS = new ArrayList<>();
        for(Comments c : commentRepository.findAllByArticle_ArticleUUID(uuid)){
            CommentDTO commentDTO = ObjectMapperUtils.map(c, CommentDTO.class);
            commentDTO.setUsername(c.getUser().getUsername());
            commentDTOS.add(commentDTO);
        }

        return commentDTOS;
    }

    @Override
    public void createComment(CommentDTO commentDTO, User user, Article article) throws PersistenceException {

        Comments comments = ObjectMapperUtils.map(commentDTO, Comments.class);
        comments.setCommentUUID(UUID.randomUUID().toString());
        comments.setUser(user);
        comments.setArticle(article);
        commentRepository.save(comments);
    }

    @Override
    public Comments getCommentByUUID(String uuid) {
        return ObjectMapperUtils.map(commentRepository.findByCommentUUID(uuid), Comments.class);
    }

    @Override
    public void updateComment(CommentDTO commentDTO) throws PersistenceException {
        commentRepository.updateComment(commentDTO.getComment(), commentDTO.getCommentUUID());
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteComment(String uuid) {
        commentRepository.deleteComment(uuid);
    }
}

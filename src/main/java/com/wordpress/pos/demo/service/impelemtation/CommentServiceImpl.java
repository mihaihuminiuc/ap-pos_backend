package com.wordpress.pos.demo.service.impelemtation;

import com.wordpress.pos.demo.dto.CommentDTO;
import com.wordpress.pos.demo.model.Article;
import com.wordpress.pos.demo.model.Comments;
import com.wordpress.pos.demo.repository.CommentRepository;
import com.wordpress.pos.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
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
    public List<Comments> getAllCommentsForArticleId(long id) {
        return commentRepository.findAll();
    }

    @Override
    public void saveComment(CommentDTO commentDTO, Article article) throws PersistenceException {
        Comments comments =  new Comments();

        comments.setArticle(article);
        comments.setComment(commentDTO.getComment());
        comments.setCorrId(UUID.randomUUID());

        commentRepository.save(comments);
    }

    @Override
    public void updateArticle(CommentDTO commentDTO) throws PersistenceException {
        Comments comments =  new Comments();

        comments.setComment(commentDTO.getComment());

        commentRepository.save(comments);
    }
}

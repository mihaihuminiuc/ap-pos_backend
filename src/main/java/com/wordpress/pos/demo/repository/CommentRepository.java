package com.wordpress.pos.demo.repository;

import com.wordpress.pos.demo.model.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comments, Long> {
    List<Comments> findAllByArticle_ArticleUUID(String uuid);
    List<Comments> findById(long id);
}

package com.wordpress.pos.demo.repository;

import com.wordpress.pos.demo.dto.CommentDTO;
import com.wordpress.pos.demo.model.Comments;
import com.wordpress.pos.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comments, Long> {

    List<Comments> findAllByArticle_ArticleUUID(String uuid);
    Comments findByCommentUUID(String uuid);

    @Modifying
    @Transactional
    @Query("UPDATE Comments c SET c.comment = :comment WHERE c.commentUUID = :commentUUID")
    void updateComment(
            @Param("comment") String comment,
            @Param("commentUUID") String commentUUID
    );

    @Modifying
    @Transactional
    @Query("DELETE FROM Comments WHERE commentUUID = :commentUUID")
    void deleteComment(
            @Param("commentUUID") String commentUUID
    );
}

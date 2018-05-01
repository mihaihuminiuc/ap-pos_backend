package com.wordpress.pos.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "COMMENTS")
public class Comments {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCommentUUID() {
        return commentUUID;
    }

    public void setCommentUUID(String commentUUID) {
        this.commentUUID = commentUUID;
    }

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(name = "comment", length = 50, nullable=false)
    @NotNull
    @Size(min = 4, max = 50)
    private String comment;
    
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="article_uuid", nullable=false)
    private Article article;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="user_uuid", nullable=false)
    private User user;

    @Column(name = "comment_uuid", nullable=false)
    private String commentUUID;
}

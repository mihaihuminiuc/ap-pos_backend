package com.wordpress.pos.demo.model;

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

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(name = "comment", length = 50, unique = true)
    @NotNull
    @Size(min = 4, max = 50)
    private String comment;
    
    @ManyToOne
    @JoinColumn(name="article_id", nullable=false)
    private Article article;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

}

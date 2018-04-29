package com.wordpress.pos.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "ARTICLE")
public class Article {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public UUID getCorrId() {
        return corrId;
    }

    public void setCorrId(UUID corrId) {
        this.corrId = corrId;
    }

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Lob
    @Column(name = "article_content")
    @NotNull
    @Size(min = 100)
    private String articleContent;

    @Column(name = "article_title")
    @NotNull
    @Size(min = 10, max  = 50)
    private String articleTitle;

    @OneToMany(mappedBy="article", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Comments> comments;

    @Column(name = "article_uuid")
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private UUID corrId;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    @JsonBackReference
    private User user;

}

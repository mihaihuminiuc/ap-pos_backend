package com.wordpress.pos.demo.dto.article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.wordpress.pos.demo.model.Comments;

import java.util.List;

public class ArticleDTO {

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

    public ArticleDTO getArticleDTO() {
        return articleDTO;
    }

    public void setArticleDTO(ArticleDTO articleDTO) {
        this.articleDTO = articleDTO;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleUUID() {
        return articleUUID;
    }

    public void setArticleUUID(String articleUUID) {
        this.articleUUID = articleUUID;
    }

    public ArticleDTO(String response){
        Gson gson = new Gson();
        this.articleDTO = gson.fromJson(response,ArticleDTO.class);
    }

    public ArticleDTO(){}

    @JsonIgnore
    private ArticleDTO articleDTO;

    @SerializedName("article_title")
    private String articleTitle;

        @SerializedName("article_content")
    private String articleContent;

    @SerializedName("commnets")
    private List<Comments> comments;

    @SerializedName("article_uuid")
    private String articleUUID;
}

package com.wordpress.pos.demo.dto;

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

    public List<Comments> getCommentsList() {
        return commentsList;
    }

    public void setCommentsList(List<Comments> commentsList) {
        this.commentsList = commentsList;
    }

    public ArticleDTO getArticleDTO() {
        return articleDTO;
    }

    public void setArticleDTO(ArticleDTO articleDTO) {
        this.articleDTO = articleDTO;
    }

    public int getArticleID() {
        return articleID;
    }

    public void setArticleID(int articleID) {
        this.articleID = articleID;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public ArticleDTO(String response){
        Gson gson = new Gson();
        this.articleDTO = gson.fromJson(response,ArticleDTO.class);
    }

    private ArticleDTO articleDTO;

    @SerializedName("article_id")
    private int articleID;

    @SerializedName("article_title")
    private String articleTitle;

    @SerializedName("article_content")
    private String articleContent;

    @SerializedName("commnets")
    private List<Comments> commentsList;
}

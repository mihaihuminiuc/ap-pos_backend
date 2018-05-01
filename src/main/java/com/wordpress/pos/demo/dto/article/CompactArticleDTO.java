package com.wordpress.pos.demo.dto.article;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class CompactArticleDTO {

    public CompactArticleDTO(){}

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

    @SerializedName("article_title")
    private String articleTitle;

    @SerializedName("article_uuid")
    private String articleUUID;
}

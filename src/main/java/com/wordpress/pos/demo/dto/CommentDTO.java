package com.wordpress.pos.demo.dto;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class CommentDTO {

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public CommentDTO getCommentDTO() {
        return commentDTO;
    }

    public void setCommentDTO(CommentDTO commentDTO) {
        this.commentDTO = commentDTO;
    }

    public String getArticleUUID() {
        return articleUUID;
    }

    public void setArticleUUID(String articleUUID) {
        this.articleUUID = articleUUID;
    }

    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    public CommentDTO(String response){
        Gson gson = new Gson();
        this.commentDTO = gson.fromJson(response,CommentDTO.class);
    }

    private CommentDTO commentDTO;

    @SerializedName("comment")
    private String comment;

    @SerializedName("article_uuid")
    private String articleUUID;

    @SerializedName("user_uuid")
    private String userUUID;
}

package com.wordpress.pos.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    public String getCommentUUID() {
        return commentUUID;
    }

    public void setCommentUUID(String commentUUID) {
        this.commentUUID = commentUUID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public CommentDTO(){}

    public CommentDTO(String response){
        Gson gson = new Gson();
        this.commentDTO = gson.fromJson(response,CommentDTO.class);
    }

    @JsonIgnore
    private CommentDTO commentDTO;

    private String comment;

    private String articleUUID;

    @JsonIgnore
    private String userUUID;

    private String commentUUID;

    @JsonIgnore
    private String username;
}

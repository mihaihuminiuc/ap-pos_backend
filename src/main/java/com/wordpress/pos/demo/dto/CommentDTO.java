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

    public CommentDTO(String response){
        Gson gson = new Gson();
        this.commentDTO = gson.fromJson(response,CommentDTO.class);
    }

    private CommentDTO commentDTO;

    @SerializedName("comment")
    private String comment;

}

package com.wordpress.pos.demo.validator;

import com.wordpress.pos.demo.dto.CommentDTO;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class Commentvalidation implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return CommentDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CommentDTO commentDTO  = (CommentDTO) target;

        ValidationUtils.rejectIfEmpty(errors, "comment", "text.error.comment.empty");
        ValidationUtils.rejectIfEmpty(errors, "articleUUID", "text.error.comment.article_uuid");

        if(commentDTO.getComment().trim().length()<5)
            errors.rejectValue("comment","text.error.comment.toshort");
    }
}

package com.wordpress.pos.demo.validator;

import com.wordpress.pos.demo.dto.article.ArticleDTO;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class ArticleValidation implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return ArticleDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ArticleDTO articleDTO = (ArticleDTO) target;

        ValidationUtils.rejectIfEmpty(errors, "articleContent", "text.error.article.empty");
        ValidationUtils.rejectIfEmpty(errors, "articleTitle", "text.error.article.titleempty");

        if(articleDTO.getArticleTitle() == null || articleDTO.getArticleTitle().trim().length()<5)
            errors.rejectValue("articleTitle","text.error.article.titletoshort");

        if(articleDTO.getArticleContent() == null || articleDTO.getArticleContent().trim().length()<20)
            errors.rejectValue("articleContent","text.error.article.toshort");
    }
}

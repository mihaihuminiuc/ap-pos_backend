package com.wordpress.pos.demo.validator;

import com.wordpress.pos.demo.dto.AuthorityDTO;
import com.wordpress.pos.demo.dto.article.ArticleDTO;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class AuthorityValidation  implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return AuthorityDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "userAuthority", "text.error.username.authorityempty");
    }
}
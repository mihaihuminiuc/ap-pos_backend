package com.wordpress.pos.demo.validator;

import com.wordpress.pos.demo.dto.UserDTO;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return UserDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        ValidationUtils.rejectIfEmpty(errors, "username", "text.error.username.empty");
        ValidationUtils.rejectIfEmpty(errors, "password", "text.error.password.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "text.error.username.whitespaces");

        if("username".length()<3)
            errors.rejectValue("username","text.error.username.toshort");
        else if("username".length()>21)
            errors.rejectValue("username","text.error.username.tolong");

        if("username".length()<3)
            errors.rejectValue("username","text.error.password.toshort");
        else if("username".length()>100)
            errors.rejectValue("username","text.error.password.tolong");

        UserDTO userDTO = (UserDTO) o;
    }
}

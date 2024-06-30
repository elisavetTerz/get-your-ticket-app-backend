package com.getyourticket.cf.validator;

import com.getyourticket.cf.dto.UserRegisterDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserRegisterValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return UserRegisterDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserRegisterDTO userRegisterDTO = (UserRegisterDTO) target;


        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "empty");
        if (!userRegisterDTO.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errors.rejectValue("email", "format");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "empty");
        if (userRegisterDTO.getPassword().length() < 6) {
            errors.rejectValue("password", "size");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "empty");
        if (userRegisterDTO.getUsername().length() < 3 || userRegisterDTO.getUsername().length() > 50) {
            errors.rejectValue("username", "size");
        }
    }
}

package com.getyourticket.cf.validator;

import com.getyourticket.cf.dto.UserLoginDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserLoginValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return UserLoginDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserLoginDTO userLoginDTO = (UserLoginDTO) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "empty");
        if (userLoginDTO.getUsername().length() < 3 || userLoginDTO.getUsername().length() > 50) {
            errors.rejectValue("username", "size");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "empty");
        if (userLoginDTO.getPassword().length() < 6) {
            errors.rejectValue("password", "size");
        }
    }
}

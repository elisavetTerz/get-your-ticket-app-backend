package com.getyourticket.cf.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UserUpdateValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {

    }

    @Override
    public Errors validateObject(Object target) {
        return Validator.super.validateObject(target);
    }
}

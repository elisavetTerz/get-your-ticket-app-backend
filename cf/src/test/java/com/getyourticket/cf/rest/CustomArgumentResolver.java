package com.getyourticket.cf.rest;

import com.getyourticket.cf.dto.UserRegisterDTO;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class CustomArgumentResolver implements HandlerMethodArgumentResolver {

    private final WebBindingInitializer initializer;

    public CustomArgumentResolver(WebBindingInitializer initializer) {
        this.initializer = initializer;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return UserRegisterDTO.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, org.springframework.web.bind.support.WebDataBinderFactory binderFactory) throws Exception {
        WebDataBinder binder = binderFactory.createBinder(webRequest, null, parameter.getParameterName());
        initializer.initBinder(binder);
        return binder.getTarget();
    }
}

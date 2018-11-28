package com.fidenz.academy.rest;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RequestParamNormalizer {

    @Pointcut("execution(* (com.fidenz.academy.rest.*).*(..))")
    public void restAdvice() {
    }

    @Before("restAdvice()")
    public void normalizeParameters() {
        //TODO: add some mechanism to scan for request body params and inject it to an actual param
    }
}

package com.fidenz.academy.log;

import com.fidenz.academy.util.RequestUtil;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;

@Aspect
@Component
public class LoggingAspect {

    private Logger log = Logger.getLogger(LoggingAspect.class.getName());

    //before all endpoint execution in rest package
    @Pointcut("execution(* (com.fidenz.academy.rest.*).*(..))")
    public void restAdvice() {
    }

    //get HTTP Request related data.returns [HTTP method, remoteAddr, requested_endpoint_path]
    public String[] getRequestData(Class<?> annotationType, MethodSignature methodSignature) {
        String[] mappings = {};
        String remoteAddr = RequestUtil.getCurrentRequest().getRemoteAddr();
        String method = "";

        if (annotationType.equals(RequestMapping.class)) {
            RequestMapping requestMapping = methodSignature.getMethod().getAnnotation(RequestMapping.class);
            mappings = requestMapping.value();
            if (mappings.length == 0) {
                mappings = requestMapping.path();
            }
            method = "GENERIC";
        } else if (annotationType.equals(GetMapping.class)) {
            GetMapping getMapping = methodSignature.getMethod().getAnnotation(GetMapping.class);
            mappings = getMapping.value();
            if (mappings.length == 0) {
                mappings = getMapping.path();
            }
            method = "GET";
        } else if (annotationType.equals(PostMapping.class)) {
            PostMapping postMapping = methodSignature.getMethod().getAnnotation(PostMapping.class);
            mappings = postMapping.value();
            if (mappings.length == 0) {
                mappings = postMapping.path();
            }
            method = "POST";
        } else if (annotationType.equals(PutMapping.class)) {
            PutMapping putMapping = methodSignature.getMethod().getAnnotation(PutMapping.class);
            mappings = putMapping.value();
            if (mappings.length == 0) {
                mappings = putMapping.path();
            }
            method = "PUT";
        } else if (annotationType.equals(DeleteMapping.class)) {
            DeleteMapping deleteMapping = methodSignature.getMethod().getAnnotation(DeleteMapping.class);
            mappings = deleteMapping.value();
            if (mappings.length == 0) {
                mappings = deleteMapping.path();
            }
            method = "DELETE";
        }
        return new String[]{method, remoteAddr, mappings[0]};
    }

    @Before("restAdvice() && (@annotation(org.springframework.web.bind.annotation.RequestMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping))")
    public void logBeforeRequest(JoinPoint joinPoint) {
        Class<?> annotationType = RequestMapping.class;
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Annotation[] annotations = methodSignature.getMethod().getAnnotations();
        annotationType = annotations[0].annotationType();

        String[] requestData = getRequestData(annotationType, methodSignature);
        log.info("REQUEST::: " + requestData[0] + " request incoming from " + requestData[1] + " at path '" + requestData[2] + "'");
    }

    @AfterReturning("restAdvice() && (@annotation(org.springframework.web.bind.annotation.RequestMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping))")
    public void logAfterRequest(JoinPoint joinPoint) {
        Class<?> annotationType = RequestMapping.class;
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Annotation[] annotations = methodSignature.getMethod().getAnnotations();
        annotationType = annotations[0].annotationType();

        String[] requestData = getRequestData(annotationType, methodSignature);
        log.info("RESPONSE::: Response corresponding to endpoint: '" + requestData[2] + "' sent to " + requestData[1]);
    }


}

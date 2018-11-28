package com.fidenz.academy.util.proxy;

import com.fidenz.academy.util.RequestUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@Aspect
@Component
public class RequestParamNormalizer {

    //before any rest method
    @Pointcut("execution(@(@org.springframework.web.bind.annotation.RequestMapping *) * *(..))")
    public void restAdvice() {
    }

    @Around("restAdvice()")
    public Object normalizeParameters(ProceedingJoinPoint joinPoint) throws Throwable {
        //get method signature
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        //get argument values
        Object[] args = joinPoint.getArgs();
        //get request names from the RequestParam annotation
        List tempArgList = new ArrayList();
        Parameter[] params = methodSignature.getMethod().getParameters();
        for (int i = 0; i < params.length; i++) {
            tempArgList.add(params[i].getAnnotation(RequestParam.class).value());
        }

        //if there are no annotations found, load java argument names
        if(tempArgList.size() == 0){
            tempArgList = Arrays.asList(methodSignature.getParameterNames());
        }
        //get current http request
        HttpServletRequest request = RequestUtil.getCurrentRequest();
        //get request body
        try {
            Scanner s = new Scanner(request.getInputStream(), "UTF-8").useDelimiter("\\A");
            String requestBody = s.hasNext() ? s.next() : "";
            JSONObject requestBodyJson = new JSONObject(requestBody);
            //check if request body contains request parameter those requested by the endpoint
            for (int i = 0; i < args.length; i++) {
                //if contains, then manipulate arguments
                if (requestBody.contains(tempArgList.get(i).toString())) {
                    args[i] = requestBodyJson.get(String.valueOf(tempArgList.get(i)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //proceed method execution
        return joinPoint.proceed(args);
    }
}

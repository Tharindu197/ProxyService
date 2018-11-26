package com.fidenz.academy.util;

import org.springframework.web.client.RestTemplate;

public class ApiCallProcessor<T> {
    public static <T extends Object> T processApiCall(String URL, Class<T> responseType) {
        RestTemplate restTemplate = new RestTemplate();
        System.out.println("UUUUUUUUUUUUUUUUUU"+URL);
        return restTemplate.getForObject(URL, responseType);
    }
}

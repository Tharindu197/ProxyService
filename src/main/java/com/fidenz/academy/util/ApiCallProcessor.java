package com.fidenz.academy.util;

import org.springframework.web.client.RestTemplate;

public class ApiCallProcessor<T> {
    public static <T extends Object> T processApiCall(String URL, Class<T> responseType) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(URL, responseType);
    }
}

package com.fidenz.academy.util;

import org.springframework.util.DigestUtils;

public class ExternalApis {
    public static final String OPENWEATHER_API(){
        String server = "http://api.openweathermap.org/data/2.5/group";
        String units = "metric";
        String appId = "9a65d50b7e563dbdb590192df2036d11";
        URLFormatter urlFormatter = new URLFormatter(server);
        urlFormatter.addRequestParam("units", units);
        urlFormatter.addRequestParam("appid", appId);
        return urlFormatter.getURL();
    }

    public static final String MARVEL_API_STORIES() {
        String publicKey = "4e89d633cfc27eb097e073f9f27fd956";
        String privateKey = "8c674d324c4b73f7577a22e7a2ad6d1170b5aebe";
        long timestamp = System.currentTimeMillis();
        String combination = timestamp + privateKey + publicKey;
        String hash = DigestUtils.md5DigestAsHex(combination.getBytes());
        String server = "http://gateway.marvel.com/v1/public/stories";
        URLFormatter urlFormatter = new URLFormatter(server);
        urlFormatter.addRequestParam("apikey", publicKey);
        urlFormatter.addRequestParam("ts",timestamp);
        urlFormatter.addRequestParam("hash", hash);
        return urlFormatter.getURL();
    }
}

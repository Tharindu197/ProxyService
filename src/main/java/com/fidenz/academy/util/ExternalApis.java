package com.fidenz.academy.util;

import org.springframework.util.DigestUtils;

public class ExternalApis {
    public static final String OPENWEATHER_API = "http://api.openweathermap.org/data/2.5/group?units=metric&appid=9a65d50b7e563dbdb590192df2036d11";

    public static final String MARVEL_API_STORIES() {
        String publicKey = "4e89d633cfc27eb097e073f9f27fd956";
        String privateKey = "8c674d324c4b73f7577a22e7a2ad6d1170b5aebe";
        long ts = System.currentTimeMillis();
        String combin = ts + privateKey + publicKey;
        String hash = DigestUtils.md5DigestAsHex(combin.getBytes());
        return "http://gateway.marvel.com/v1/public/stories?apikey=" + publicKey + "&ts=" + ts + "&hash=" + hash;
    }
}

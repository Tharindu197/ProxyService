package com.fidenz.academy.rest;

import com.fidenz.academy.entity.response.Element;
import com.fidenz.academy.services.IWebProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/proxy")
public class ProxyController {

    @Autowired
    private IWebProxyService webProxyService;

    @GetMapping("/weather")
    public Element getWeather(@RequestParam(value = "city", required = true) int cityID) {
        return webProxyService.getWeather(cityID);
    }
}

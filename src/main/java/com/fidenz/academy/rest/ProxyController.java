package com.fidenz.academy.rest;

import com.fidenz.academy.entity.response.marvel.Story;
import com.fidenz.academy.entity.response.weather.Element;
import com.fidenz.academy.services.IWebProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/proxy")
public class ProxyController {

    @Autowired
    private IWebProxyService webProxyService;

    @GetMapping("/weather")
    public Element getWeather(@RequestParam(value = "city", required = true) int cityID) {
        return webProxyService.getWeather(cityID);
    }

    @GetMapping("/marvel/stories")
    public List<Story> getStories() {
        return webProxyService.getMarvelStories();
    }
}

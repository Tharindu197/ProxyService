package com.fidenz.academy.rest;

import com.fidenz.academy.entity.response.marvel.Story;
import com.fidenz.academy.entity.response.weather.Element;
import com.fidenz.academy.services.IWebProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/proxy")
public class ProxyController {

    @Autowired
    private IWebProxyService webProxyService;

    @GetMapping("/weather")
    public Element getWeather(@RequestParam(value = "city", required = false) int cityID) {
        return webProxyService.getWeather(cityID);
    }

    @GetMapping("/marvel/stories")
    public List<Story> getStories() {
        return webProxyService.getMarvelStories();
    }

    @PostMapping("/weather")
    public Element getWeatherByPost(@RequestParam(value = "city", required = false) Integer cityID) {
        return webProxyService.getWeather(cityID);
    }
}

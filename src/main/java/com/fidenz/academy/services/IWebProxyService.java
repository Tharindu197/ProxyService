package com.fidenz.academy.services;

import com.fidenz.academy.entity.response.marvel.Story;
import com.fidenz.academy.entity.response.weather.Element;

import java.util.List;

public interface IWebProxyService {
    public Element getWeather(int cityID);

    public List<Story> getMarvelStories();
}

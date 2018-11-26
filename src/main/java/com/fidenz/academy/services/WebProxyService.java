package com.fidenz.academy.services;

import com.fidenz.academy.entity.response.marvel.Story;
import com.fidenz.academy.entity.response.weather.Element;
import com.fidenz.academy.util.ExternalApis;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebProxyService extends WebProxyServiceHelper implements IWebProxyService {

    @SuppressWarnings("unchecked")
    @Override
    public Element getWeather(int cityID) {
        String URL = ExternalApis.OPENWEATHER_API + "&id=" + cityID;
        return (Element) getData(URL, com.fidenz.academy.entity.response.weather.Response.class, Element.class, cityID, "id", "list");
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Story> getMarvelStories() {
        String URL = ExternalApis.MARVEL_API_STORIES();
        return (List<Story>) getData(URL, com.fidenz.academy.entity.response.marvel.Response.class, Story.class, "data.results");
    }
}

package com.fidenz.academy.services;

import com.fidenz.academy.entity.response.marvel.Story;
import com.fidenz.academy.entity.response.weather.Element;
import com.fidenz.academy.util.ExternalApis;
import com.fidenz.academy.util.URLFormatFactory;
import com.fidenz.academy.util.URLFormatter;
import com.fidenz.academy.util.proxy.WebProxyServiceHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebProxyService extends WebProxyServiceHelper implements IWebProxyService {

    @SuppressWarnings("unchecked")
    @Override
    public Element getWeather(int cityID) {
        URLFormatter urlFormatter = URLFormatFactory.buildFormatter(ExternalApis.OPENWEATHER_API());
        urlFormatter.addRequestParam("id", cityID);
        String URL = urlFormatter.getURL();
        //following returned object can be post-processed/ manipulated before sending back to client, if necessary
        Element element = (Element) getData(URL, com.fidenz.academy.entity.response.weather.Response.class, Element.class, cityID, "id", "list");
        return element;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Story> getMarvelStories() {
        String URL = ExternalApis.MARVEL_API_STORIES();
        //following returned list can be post-processed/ manipulated before sending back to client, if necessary
        List<Story> stories = (List<Story>) getData(URL, com.fidenz.academy.entity.response.marvel.Response.class, Story.class, "data.results");
        return stories;
    }
}

package com.fidenz.academy.services;

import com.fidenz.academy.entity.response.Element;
import com.fidenz.academy.entity.response.Response;
import com.fidenz.academy.repo.IGenericRepository;
import com.fidenz.academy.util.ApiCallProcessor;
import com.fidenz.academy.util.EntityValidator;
import com.fidenz.academy.util.ExternalApis;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class WebProxyService extends WebProxyServiceHelper implements IWebProxyService {

    @SuppressWarnings("unchecked")
    @Override
    public Element getWeather(int cityID) {
        String URL = ExternalApis.OPENWEATHER_API + "&id=" + cityID;
        return (Element) getData(URL, Response.class, Element.class, cityID, "id", "list");
    }
}

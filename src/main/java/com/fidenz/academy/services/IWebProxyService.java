package com.fidenz.academy.services;

import com.fidenz.academy.entity.response.Element;

public interface IWebProxyService {
    public Element getWeather(int cityID);
}

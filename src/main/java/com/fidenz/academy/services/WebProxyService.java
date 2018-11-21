package com.fidenz.academy.services;

import com.fidenz.academy.entity.response.Element;
import com.fidenz.academy.entity.response.Response;
import com.fidenz.academy.repo.IGenericRepository;
import com.fidenz.academy.util.ApiCallProcessor;
import com.fidenz.academy.util.EntityValidator;
import com.fidenz.academy.util.ExternalApis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class WebProxyService implements IWebProxyService {

    @Autowired
    private IGenericRepository repository;

    @Override
    public Element getWeather(int cityID) {
        List<Element> elements = repository.retrieveResponses();
        Element element = null;

        if(elements == null || elements.size() == 0 || lookupCity(elements, cityID) == null ){ //no records in the db, call external api
            element = fetchToDB(cityID);
        } else {
            element = lookupCity(elements, cityID);
            if(EntityValidator.isExpired(element)){ //expired. delete entity and call api
                repository.delete(element);
                element = fetchToDB(cityID);
            }
        }
        return element;
    }

    private Element fetchToDB(int cityID){
        Response response = ApiCallProcessor.processApiCall(ExternalApis.OPENWEATHER_API + "&id=" + cityID, Response.class);
        Element element = null;
        if(response != null) {
            element = response.getList().get(0);
            element.setTimestamp(new Date());
            repository.save(element);
        }
        return element;
    }

    private Element lookupCity(List<Element> elements, int cityID){
        for(Element element: elements){
            if(element != null && element.getId() == cityID){
                return element;
            }
        }
        return null;
    }
}

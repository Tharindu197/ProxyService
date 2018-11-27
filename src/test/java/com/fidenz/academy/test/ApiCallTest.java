package com.fidenz.academy.test;

import com.fidenz.academy.entity.response.weather.Response;
import com.fidenz.academy.util.ApiCallProcessor;
import com.fidenz.academy.util.ExternalApis;
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;

public class ApiCallTest {

    @Test
    public void testApiCalls(){
        assertNotNull(ApiCallProcessor.processApiCall(ExternalApis.OPENWEATHER_API() +"&id=524901", Response.class));
        assertNotNull(ApiCallProcessor.processApiCall(ExternalApis.MARVEL_API_STORIES(), com.fidenz.academy.entity.response.marvel.Response.class));
    }
}

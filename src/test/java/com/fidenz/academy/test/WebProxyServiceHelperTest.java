package com.fidenz.academy.test;

import com.fidenz.academy.entity.response.marvel.Story;
import com.fidenz.academy.entity.response.weather.Element;
import com.fidenz.academy.util.proxy.WebProxyServiceHelper;
import com.fidenz.academy.test.com.fidenz.academy.test.config.TestSpringConfig;
import com.fidenz.academy.util.ExternalApis;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = TestSpringConfig.class)
public class WebProxyServiceHelperTest extends WebProxyServiceHelper {

    @Test
    public void testHelperService() {
        //check if cached data or newly fetched data returns
        List<Story> stories = (List<Story>) getData(ExternalApis.MARVEL_API_STORIES(), com.fidenz.academy.entity.response.marvel.Response.class, Story.class, "data.results");
        assertNotNull((Element) getData(ExternalApis.OPENWEATHER_API() + "&id=524901", com.fidenz.academy.entity.response.weather.Response.class, Element.class, 524901, "id", "list"));
        assertNotNull(stories);
        //validate returned data
        assertEquals(524901, ((Element) getData(ExternalApis.OPENWEATHER_API() + "&id=524901", com.fidenz.academy.entity.response.weather.Response.class, Element.class, 524901, "id", "list")).getId());
        assertTrue(stories.size() > 0);
    }
}

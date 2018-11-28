package com.fidenz.academy.test;

import com.fidenz.academy.entity.response.marvel.Story;
import com.fidenz.academy.repo.IGenericRepository;
import com.fidenz.academy.test.com.fidenz.academy.test.config.TestSpringConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = TestSpringConfig.class)
public class DbRepositoryTest extends AbstractJUnit4SpringContextTests {
    @Autowired
    private IGenericRepository repository;

    @Test
    public void testGenericEntityAndDbRepo() {
        Story story = new Story();
        story.setTitle("Test Title");
        story.setId(99999);
        //get from db
        List<Story> testEntityList = repository.retrieveResponses();
        assertNotNull(testEntityList);
        assertTrue(testEntityList.size() > 0);
        repository.delete(story);
    }
}

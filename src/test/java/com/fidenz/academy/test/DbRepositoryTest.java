package com.fidenz.academy.test;

import com.fidenz.academy.repo.IGenericRepository;
import com.fidenz.academy.test.com.fidenz.academy.test.config.TestSpringConfig;
import com.fidenz.acadmy.test.entity.TestEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes= TestSpringConfig.class)
public class DbRepositoryTest extends AbstractJUnit4SpringContextTests {
    @Autowired
    private IGenericRepository repository;

    @Test
    public void testGenericEntityAndDbRepo(){
        TestEntity testEntity = new TestEntity();
        testEntity.setAge(10);
        testEntity.setName("test");
        testEntity.setSalary(123.43);
        //save in the db
        repository.save(testEntity);
        //get from db
        List<TestEntity> testEntityList = repository.retrieveResponses();
        assertNotNull( testEntityList);
        assertEquals(true, testEntityList.size()> 0);
        assertEquals("test", testEntityList.get(0).getName());
    }
}

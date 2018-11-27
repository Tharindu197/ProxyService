package com.fidenz.academy.test;

import com.fidenz.academy.Application;
import com.fidenz.academy.repo.IGenericRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import static junit.framework.TestCase.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes=Application.class)
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
        assertEquals(1, testEntityList.size());
        assertEquals("test", testEntityList.get(0).getName());
    }
}

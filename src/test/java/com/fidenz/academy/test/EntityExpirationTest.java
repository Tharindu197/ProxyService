package com.fidenz.academy.test;

import com.fidenz.academy.test.com.fidenz.academy.test.config.TestSpringConfig;
import com.fidenz.academy.util.EntityValidator;
import com.fidenz.acadmy.test.entity.TestEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = TestSpringConfig.class)
public class EntityExpirationTest {

    @Test
    public void testEntityExpiration() {

        //set fake timestamp to the entity that might be expired
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 01);
        cal.set(Calendar.MINUTE, 30);
        cal.set(Calendar.SECOND, 0);

        TestEntity testEntity = new TestEntity();
        testEntity.setAge(10);
        testEntity.setName("test");
        testEntity.setSalary(123.43);
        testEntity.setTimestamp(cal.getTime());

        //validate
        assertTrue(EntityValidator.isExpired(testEntity));

        //update entity time to current time: then entity might not be expired
        testEntity.setTimestamp(new Date());
        assertFalse(EntityValidator.isExpired(testEntity));
    }
}

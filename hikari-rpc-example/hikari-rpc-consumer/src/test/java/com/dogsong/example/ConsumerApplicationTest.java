package com.dogsong.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-config.xml")
public class ConsumerApplicationTest {

    @Resource
    private ExampleService exampleService;

    @Test
    public void hi() {
        String hi = exampleService.helloWorld("1111");
        System.out.println(hi);
    }

}
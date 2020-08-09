package com.fh.shop.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Resource;


@SpringBootTest
@ContextConfiguration("classpath:application.properties")
public class MongoController {

    @Resource
    private   MongoTemplate template;


    @Test
    public void add(){
        User user = new User();
        user.setUserName("test");
        user.setSex("男");
        template.insert(user);
    }
}

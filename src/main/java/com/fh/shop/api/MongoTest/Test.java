package com.fh.shop.api.MongoTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("test")
public class Test {
    private static final String ID_FIELD = "_id";

   /* @Resource
    @Qualifier("mongoTemplate")
    private MongoTemplate template;

    @RequestMapping("add")
    public void  testAdd(User user){

        template.insert(user);
    }*/

   @Autowired(required = false)
    private MongoTemplate mongoTemplate;

   @RequestMapping("add")
   public void add(User user){
       mongoTemplate.save(user);
   }

   @RequestMapping("query")
   public List<User> query( ){
       List<User> all = mongoTemplate.findAll(User.class);
       return  all;
   }
}

package com.cosmos.journalApp.repository;

import com.cosmos.journalApp.entity.users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class userrepositoryimpl {
@Autowired
    private MongoTemplate mongoTemplate;

public List<users> getusersforsa(){
    Query query=new Query();
    query.addCriteria(new Criteria().andOperator(
            Criteria.where("email").regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"),
            Criteria.where("sentimentAnalysis").is(true)
    ));


    return mongoTemplate.find(query,users.class);

}



}

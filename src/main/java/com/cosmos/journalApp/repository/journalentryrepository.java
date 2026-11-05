package com.cosmos.journalApp.repository;

import com.cosmos.journalApp.entity.journalentry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;


public interface journalentryrepository extends MongoRepository<journalentry, ObjectId> {


    static void DeleteById(ObjectId id) {
    }
}

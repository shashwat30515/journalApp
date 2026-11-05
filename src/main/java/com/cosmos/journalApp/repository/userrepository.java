package com.cosmos.journalApp.repository;

import com.cosmos.journalApp.entity.users;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface userrepository extends MongoRepository<users, ObjectId> {
    users findByusername(String username);

    void deleteByUsername(String name);
}



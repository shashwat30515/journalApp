package com.cosmos.journalApp.repository;

import com.cosmos.journalApp.entity.configjournalappentity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface configjournalrepo extends MongoRepository<configjournalappentity, ObjectId> {
}

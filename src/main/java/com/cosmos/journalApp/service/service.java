package com.cosmos.journalApp.service;

import com.cosmos.journalApp.entity.journalentry;
import com.cosmos.journalApp.entity.users;
import com.cosmos.journalApp.repository.journalentryrepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;




@Component
public class service {

    @Autowired
    private journalentryrepository journalentryrepository;
    @Autowired
    private userservice userservice;

    private static final Logger logger = LoggerFactory.getLogger(service.class);


@Transactional
 public void saveentry(journalentry journalentry, String username){
    try {
        users user = userservice.findusername(username);
        journalentry.setDate(LocalDateTime.now());
        journalentry saved = journalentryrepository.save(journalentry);
        user.getJournalentry().add(saved);

        userservice.saveuser(user);
    }
    catch(Exception e){
        logger.info("eeeeeeeeeeeeee");

        throw new RuntimeException("error occured while saving the entry",e);
    }
 }
    public void saveentry(journalentry journalentry){
      journalentryrepository.save(journalentry);
    }
 public List<journalentry> getjournalentries(){
     return journalentryrepository.findAll();
 }
public Optional< journalentry> findbyid(ObjectId id) {

    return journalentryrepository.findById(id);
}
@Transactional
public boolean deletejournalentry(ObjectId id, String username){
    boolean removed=false;
     users user=userservice.findusername(username);
      removed =user.getJournalentry().removeIf(x->x.getId().equals(id));
    try {
        if (removed) {
            userservice.saveuser(user);
            journalentryrepository.deleteById(id);
        }

    }
    catch(Exception e){
        throw new RuntimeException("error occured while deleting the entry",e);
    }
    return removed;

}
//public List<journalentry>findbyusername(String username){
//
//
//}

}

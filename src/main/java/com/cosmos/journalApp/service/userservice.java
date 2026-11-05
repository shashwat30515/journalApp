package com.cosmos.journalApp.service;

import com.cosmos.journalApp.entity.users;
import com.cosmos.journalApp.repository.userrepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
@Component
@Slf4j
public class userservice {

    @Autowired
    private userrepository userrepository;

    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();



    public boolean savenewuser(users users) {
        try {
            users.setPassword(encoder.encode(users.getPassword()));
            users.setRoles(Arrays.asList("USER"));



            userrepository.save(users);
            return true;
        } catch (Exception e) {
            e.printStackTrace();   // or log.error(...), so failures are visible
            return false;
        }
    }
    public void saveadmin(users users){

      users.setPassword(encoder.encode(users.getPassword()));
      users.setRoles(Arrays.asList("USER","ADMIN"));
        userrepository.save(users);
    }
    public void saveuser(users users){

        userrepository.save(users);
    }

    public List<users > getall(){
        return userrepository.findAll();
    }

    public Optional< users> findbyid(ObjectId id) {

        return userrepository.findById(id);
    }
    public void deleteusersentry(ObjectId id){
        userrepository.deleteById(id);
    }
    public users findusername(String username){
        return userrepository.findByusername(username);
    }
//    public void adduseradmin(users users){
//        users.setRoles(Arrays.asList("USER","ADMIN"));
//
//    }
}

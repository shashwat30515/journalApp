package com.cosmos.journalApp.controller;
import com.cosmos.journalApp.entity.users;
import com.cosmos.journalApp.service.userservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class Admincontroller {
    @Autowired
    userservice userservice;
    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers() {
        List<users> all = userservice.getall();
        if(all!=null && !all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
    @PostMapping("/create-admin-user")
    public ResponseEntity<?> createAdminUser(@RequestBody users user) {
     try {
         userservice.saveadmin(user);
         return new ResponseEntity<>(HttpStatus.CREATED);
     }
     catch (Exception e) {
         return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
     }
    }

}

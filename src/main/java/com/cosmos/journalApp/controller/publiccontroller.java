package com.cosmos.journalApp.controller;

import com.cosmos.journalApp.entity.users;
import com.cosmos.journalApp.service.userservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")


public class publiccontroller {


    @GetMapping("/health-check")
    public String check() {
        return "ok";
    }


    @Autowired
    private userservice userservice;


    @PostMapping("/create-user")
    public ResponseEntity<?> addusersentry(@RequestBody users users){
        try {
            userservice.savenewuser(users);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

}



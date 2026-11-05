package com.cosmos.journalApp.controller;


import com.cosmos.journalApp.entity.users;
import com.cosmos.journalApp.service.customuserdetailserviceimpl;
import com.cosmos.journalApp.service.service;
import com.cosmos.journalApp.service.userservice;

import com.cosmos.journalApp.utils.jwtutils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/public")

@Slf4j

public class publiccontroller {


    @GetMapping("/health-check")
    public String check() {
        return "ok";
    }


    @Autowired
    private userservice userservice;
   @Autowired
   private service journalentryservice;
   @Autowired
   private AuthenticationManager authenticationManager;
   @Autowired
   private customuserdetailserviceimpl userdetailserviceimpl;
   @Autowired
   private jwtutils jwtutils;

    @PostMapping("/create-user")
    public ResponseEntity<?> addusersentry(@RequestBody users users) {
        try {
            userservice.savenewuser(users);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody users users) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(users.getUsername(), users.getPassword()));
          UserDetails userDetails = userdetailserviceimpl.loadUserByUsername(users.getUsername());
         String jwt = jwtutils.generateToken(userDetails.getUsername());
         return new ResponseEntity<>(jwt, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Exception While createAuthenticationtoken",e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }
    }




}










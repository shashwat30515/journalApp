package com.cosmos.journalApp.controller;

import com.cosmos.journalApp.apiresponse.weatherresponse;
import com.cosmos.journalApp.entity.users;
import com.cosmos.journalApp.repository.userrepository;
import com.cosmos.journalApp.service.userservice;
import com.cosmos.journalApp.service.weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class usercontroller {
@Autowired
private userservice userservice;
@Autowired
private userrepository userrepo;
//@GetMapping
//public List<users>getallusers(){
//    return userservice.getuserentries();
//
//}
    @Autowired
    private weather weather;

@PutMapping
    public ResponseEntity<?>updateusersentry( @RequestBody users users  ){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String Username = authentication.getName();
    users userindb  = userservice.findusername(Username);

        userindb.setUsername(users.getUsername());
        userindb.setPassword(users.getPassword());
        userservice.savenewuser(userindb);


    return new ResponseEntity(HttpStatus.NO_CONTENT);
}
@DeleteMapping
public ResponseEntity<?>deleteusersentry(   ){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

   userrepo.deleteByUsername(authentication.getName());

    return new ResponseEntity(HttpStatus.NO_CONTENT);
}





    @GetMapping("/greeting")
    public ResponseEntity<?>greetingwithweather(  ) {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            weatherresponse weatherresponse = weather.getWeather("Mumbai");
            String greeting = "";
            if (weatherresponse != null) {
                greeting = ", weather feels like " + weatherresponse.getCurrent().getFeelslike();
            }
            return new ResponseEntity("Hi " + authentication.getName() + greeting, HttpStatus.OK);
        }
       catch (Exception e) {
            return new ResponseEntity("API terms end",HttpStatus.GATEWAY_TIMEOUT);
       }
    }
    @GetMapping
    public ResponseEntity<?>greeting(  ) {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            return new ResponseEntity("Hi " + authentication.getName() , HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity("user not exsist",HttpStatus.NOT_FOUND);
        }
    }




    }





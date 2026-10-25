package com.cosmos.journalApp.controller;

import com.cosmos.journalApp.entity.journalentry;
import com.cosmos.journalApp.entity.users;
import com.cosmos.journalApp.service.service;
import com.cosmos.journalApp.service.userservice;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;


@RestController
@RequestMapping("/journal")
public class journalentrycontroller {
    @Autowired
    private service journalentryservice;
    @Autowired
    private userservice userservice;

    @GetMapping
    public ResponseEntity<?> getalljournalentriesusers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String Username = authentication.getName();
        users user=userservice.findusername(Username);
       List <journalentry>all  =user.getJournalentry();
       if(all!=null && !all.isEmpty()){
           return new ResponseEntity<>(all,HttpStatus.OK);
       }
       return new ResponseEntity<>(HttpStatus.NOT_FOUND);


    }

    @PostMapping
    public ResponseEntity<journalentry> addjournalentry(@RequestBody journalentry j ) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String Username = authentication.getName();
            users user=userservice.findusername(Username);
            j.setDate(LocalDateTime.now());

            journalentryservice.saveentry(j,Username);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }


    }

    @GetMapping("/id/{id}")
    public ResponseEntity<journalentry> getjournalentry(@PathVariable ObjectId id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String Username = authentication.getName();
      users user=userservice.findusername(Username);
      List<journalentry>collect=user.getJournalentry().stream().filter(x->x.getId().equals(id)).collect(Collectors.toList());
        Optional<journalentry> journalentry = journalentryservice.findbyid(id);
        if( !collect.isEmpty()){
            Optional<journalentry> journalentry1 = journalentryservice.findbyid(id);
            if (journalentry1.isPresent()) {
                return new ResponseEntity<>(journalentry.get(), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deletejournalentry(@PathVariable ObjectId id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String Username = authentication.getName();
       boolean removed= journalentryservice.deletejournalentry(id,Username);
       if(removed) {
           return new ResponseEntity<>(HttpStatus.NO_CONTENT);
       }
       else {
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }
    }

@PutMapping("/id/{id}")
    public ResponseEntity<?> updatejournalentry(@PathVariable ObjectId id, @RequestBody journalentry j) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String Username = authentication.getName();
    users user = userservice.findusername(Username);
    List<journalentry> collect = user.getJournalentry().stream().filter(x -> x.getId().equals(id)).collect(Collectors.toList());
    if (!collect.isEmpty()) {
        Optional<journalentry> journalentry = journalentryservice.findbyid(id);
        if (journalentry.isPresent()) {

            journalentry old = journalentry.get();
            old.setTitle(j.getTitle() != null && !j.getTitle().equals("") ? j.getTitle() : old.getTitle());
            old.setContent(j.getContent() != null && !j.getContent().equals("") ? j.getContent() : old.getContent());
            journalentryservice.saveentry(old);
            return new ResponseEntity<>(old, HttpStatus.OK);
        }


    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
}



}





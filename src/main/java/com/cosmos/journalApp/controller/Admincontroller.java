package com.cosmos.journalApp.controller;
import com.cosmos.journalApp.appcache.appcache;
import com.cosmos.journalApp.entity.journalentry;
import com.cosmos.journalApp.entity.users;
import com.cosmos.journalApp.enums.Sentiment;
import com.cosmos.journalApp.repository.journalentryrepository;
import com.cosmos.journalApp.repository.userrepository;
import com.cosmos.journalApp.repository.userrepositoryimpl;
import com.cosmos.journalApp.service.userservice;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/admin")



public class Admincontroller {
    @Autowired
    userrepository userrepository;
    @Autowired
    userservice userservice;
    @Autowired
    appcache appcache;
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

//    @PutMapping("/update-user/{id}")
//    public ResponseEntity<?> updateUserByAdmin(@PathVariable ObjectId id,
//                                               @RequestBody users newData) {
//        try{
//            if(userrepositoryimpl.finduser()=tr)
//
//        }
//
//    }
@PutMapping("/make-admin/{id}")
public ResponseEntity<?> makeAdmin(@PathVariable ObjectId id) {
    Optional<users> found = userrepository.findById(id);
    if (found.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    users user = found.get();
    List<String> roles = user.getRoles() == null
            ? new ArrayList<>()
            : new ArrayList<>(user.getRoles());

    if (!roles.contains("USER"))  roles.add("USER");
    if (!roles.contains("ADMIN")) roles.add("ADMIN");

    user.setRoles(roles);
    userrepository.save(user);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
}
    @PutMapping("/remove-admin/{id}")
    public ResponseEntity<?> removeAdmin(@PathVariable ObjectId id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Optional<users> found = userrepository.findById(id);
        if (found.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        users user = found.get();

        // an admin cannot remove their own admin role
        if (user.getUsername().equals(auth.getName())) {
            return new ResponseEntity<>("You cannot remove your own admin role", HttpStatus.BAD_REQUEST);
        }

        List<String> roles = user.getRoles() == null
                ? new ArrayList<>()
                : new ArrayList<>(user.getRoles());

        roles.removeIf(r -> r.equalsIgnoreCase("ADMIN") || r.equalsIgnoreCase("ROLE_ADMIN"));
        if (!roles.contains("USER")) roles.add("USER");   // always keep USER

        user.setRoles(roles);
        userrepository.save(user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    public record AdminJournalDTO(String id, String username, String title,
                                  String content, Object date, Sentiment sentiment) {}

    @GetMapping("/all-journal-entries")
    public ResponseEntity<List<AdminJournalDTO>> allJournalEntries() {
        List<AdminJournalDTO> result = new ArrayList<>();
        for (users u : userrepository.findAll()) {
            if (u.getJournalentry() == null) continue;
            for (journalentry e : u.getJournalentry()) {
                result.add(new AdminJournalDTO(
                        e.getId() == null ? null : e.getId().toHexString(),
                        u.getUsername(),
                        e.getTitle(),
                        e.getContent(),
                        e.getDate(),
                        e.getSentiment()));
            }
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable ObjectId id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Optional<users> found = userrepository.findById(id);
        if (found.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        users target = found.get();

        // an admin cannot delete their own account from here
        if (target.getUsername().equals(auth.getName())) {
            return new ResponseEntity<>("You cannot delete your own account", HttpStatus.BAD_REQUEST);
        }

        // delete the user's journal entries first, so none are left orphaned
        if (target.getJournalentry() != null) {
            for (journalentry e : target.getJournalentry()) {
                journalentryrepository.DeleteById(e.getId());
            }
        }

        userrepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/clear-app-cache")
    public void clearAppCache() {
        appcache.init();

    }


}

package com.cosmos.journalApp.service;

import com.cosmos.journalApp.entity.users;
import com.cosmos.journalApp.repository.userrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class customuserdetailserviceimpl implements UserDetailsService {
@Autowired
private userrepository userrepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        users user = userrepository.findByusername(username);
        if (user != null) {
            UserDetails userDetails = org.springframework.security.core.userdetails.User.builder().username(user.getUsername()).password(user.getPassword()).roles(user.getRoles().toArray(new String[0])).build();

            return userDetails;

        }
        throw new UsernameNotFoundException("User not found"+username);
    }


}

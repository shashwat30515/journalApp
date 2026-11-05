package com.cosmos.journalApp.repository;

import com.cosmos.journalApp.schedular.schedular;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class userrepotest {
    @Autowired
    schedular userschedular;
@Test
    public void testsavenewuser(){
    userschedular.fetchUserAndSendMail();
    }


}

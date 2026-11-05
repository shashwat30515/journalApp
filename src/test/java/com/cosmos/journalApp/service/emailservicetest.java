package com.cosmos.journalApp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class emailservicetest {
    @Autowired
    private emailservice emailservicetest;

    @Test
    void testmail(){

       emailservicetest.sendmail("shashwat30515@gmail.com", "greeting", "good morning ladle");


    }

}

package com.cosmos.journalApp.config;

import com.cosmos.journalApp.entity.users;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertCallback;
import org.springframework.stereotype.Component;

@Component
public class UserSentimentCallback implements BeforeConvertCallback<users> {

    @Override
    public users onBeforeConvert(users user, String collection) {
        String email = user.getEmail();
        user.setSentimentanalysis(email != null && !email.isBlank());
        return user;
    }
}
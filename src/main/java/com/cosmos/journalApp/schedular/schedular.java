package com.cosmos.journalApp.schedular;

import com.cosmos.journalApp.entity.journalentry;
import com.cosmos.journalApp.entity.users;
import com.cosmos.journalApp.enums.Sentiment;
import com.cosmos.journalApp.repository.userrepositoryimpl;
import com.cosmos.journalApp.service.emailservice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class schedular {

    private static final Logger log = LoggerFactory.getLogger(schedular.class);

    @Autowired
    private emailservice emailservice;

    @Autowired
    private userrepositoryimpl userrepository;

    // Every Sunday at 21:30 (sec min hour day month weekday)
  @Scheduled(cron = "0 30 21 ? * SUN", zone = "Asia/Kolkata")
    public void fetchUserAndSendMail() {
        List<users> allUsers = userrepository.getusersforsa();
        LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);

        for (users user : allUsers) {
            try {
                String email = user.getEmail();
                if (email == null || email.isBlank()) continue;

                List<journalentry> entries = user.getJournalentry();
                if (entries == null || entries.isEmpty()) continue;

                Map<Sentiment, Integer> counts = new EnumMap<>(Sentiment.class);
                int total = 0;
                for (journalentry e : entries) {
                    if (e.getDate() == null || e.getSentiment() == null) continue;
                    if (!e.getDate().isAfter(weekAgo)) continue;
                    counts.merge(e.getSentiment(), 1, Integer::sum);
                    total++;
                }
                if (counts.isEmpty()) continue;

                Sentiment most = null;
                int max = 0;
                for (Sentiment s : Sentiment.values()) {
                    int c = counts.getOrDefault(s, 0);
                    if (c > max) {
                        max = c;
                        most = s;
                    }
                }

                String body = "Hi " + user.getUsername() + ",\n\n"
                        + "Over the last 7 days your most frequent mood was " + most
                        + " (" + max + " of " + total + " entries).\n\n"
                        + "Keep journaling!";

                emailservice.sendmail(email, "Your mood summary for the last 7 days", body);

            } catch (Exception ex) {
                log.error("Could not send mood summary to user {}", user.getUsername(), ex);
            }
        }
    }
}
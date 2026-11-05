package com.cosmos.journalApp.appcache;

import com.cosmos.journalApp.entity.configjournalappentity;
import com.cosmos.journalApp.repository.configjournalrepo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class appcache {

    @Autowired
    configjournalrepo configjournalrepo;

    public Map<String,String> APP_CACHE ;

    @PostConstruct
    public void init(){
        APP_CACHE = new HashMap<>();
        List<configjournalappentity>all=configjournalrepo.findAll();
        for(configjournalappentity configjournalappentity1:all){
            APP_CACHE.put(configjournalappentity1.getKey(),configjournalappentity1.getValue());
        }
    }

}

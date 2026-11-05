package com.cosmos.journalApp.service;

import com.cosmos.journalApp.apiresponse.weatherresponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class redisservice {
    @Autowired
    private RedisTemplate redisTemplate;

    public <T> T get(String key, Class<T> entityClass){
       try {
           Object o = redisTemplate.opsForValue().get(key);
           ObjectMapper mapper = new ObjectMapper();
           return mapper.readValue(o.toString(), entityClass);
       }
       catch (Exception e) {
           log.error("Exception",e);
           return null;
       }
    }


    public void set (String key,Object o, long ttl){
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonvalue = mapper.writeValueAsString(o);
            redisTemplate.opsForValue().set(key,jsonvalue,ttl, TimeUnit.SECONDS);


        }
        catch (Exception e) {
            log.error("Exception",e);

        }
    }



}

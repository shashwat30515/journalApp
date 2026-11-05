package com.cosmos.journalApp.service;

import com.cosmos.journalApp.apiresponse.weatherresponse;
import com.cosmos.journalApp.appcache.appcache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class weather {
    @Value("${weather.api.key}")
    private  String apikey;


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private appcache appcache;

    @Autowired
    private redisservice redisservice;

    public weatherresponse getWeather(String city){
        weatherresponse weatherresponse =redisservice.get("weather_of_"+city,weatherresponse.class);
        if(weatherresponse!=null){return weatherresponse;}
        else {
            String finalapi = appcache.APP_CACHE.get("weather_api").replace("<city>", city).replace("<apikey>", apikey);
            ResponseEntity<weatherresponse> response = restTemplate.exchange(finalapi, HttpMethod.GET, null, weatherresponse.class);
            weatherresponse body = response.getBody();
            if(body!=null){
                redisservice.set("weather_of_"+city,body,21600l);
            }
            return body;
        }

    }
}

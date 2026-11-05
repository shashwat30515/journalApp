package com.cosmos.journalApp.apiresponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
@Getter
@Setter
public class weatherresponse{

    private Current current;


    @Getter
    @Setter

    public class Current{


        private int temperature;

        @JsonProperty("weather_descriptions")
        private List<String> weatherDescriptions;


        private int feelslike;

    }




}






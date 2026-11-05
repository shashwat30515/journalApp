package com.cosmos.journalApp.entity;


import com.cosmos.journalApp.enums.Sentiment;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;


import java.time.LocalDateTime;


@Document(collection = "journal_entries")
@Data
@NoArgsConstructor
public class journalentry {

     @Id
     @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

     private String title;
     private String content;
    private LocalDateTime date;
    private Sentiment sentiment;





}

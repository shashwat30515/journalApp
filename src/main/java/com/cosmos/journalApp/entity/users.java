package com.cosmos.journalApp.entity;

import com.cosmos.journalApp.enums.Sentiment;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;



import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Data
@NoArgsConstructor
public class users {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;


    @Indexed(unique = true)
    @NonNull
    private String username;

    @NonNull
    private String password;
    private String email;

    @DBRef
    private List<journalentry> journalentry=new ArrayList<>();
     private List<String>roles;
     private boolean sentimentanalysis;




}

package dev.hutchison.rsvp.service.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Collection;

@Data
@Document("guests")
public class GuestEntity {
    @Id
    private Long id;

    @Field("word-set")
    private String wordSet;

    private Collection<String> names;
}

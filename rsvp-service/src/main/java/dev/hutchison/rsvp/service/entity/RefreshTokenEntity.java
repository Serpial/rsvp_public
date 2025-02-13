package dev.hutchison.rsvp.service.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document("refresh-tokens")
public class RefreshTokenEntity {
    @Id
    private String id;

    private Long userId;

    private String token;

    private Date createdAt;

    private Date expiresOn;
}

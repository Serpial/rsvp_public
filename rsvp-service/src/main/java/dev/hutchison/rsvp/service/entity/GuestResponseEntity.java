package dev.hutchison.rsvp.service.entity;

import dev.hutchison.rsvp.service.model.MenuType;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document("responses")
public class GuestResponseEntity {
    @Id
    String id;
    long userId;
    long individualId;
    boolean confirmed;
    MenuType menuType;
    Date timeSent;
}

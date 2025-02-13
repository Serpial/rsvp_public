package dev.hutchison.rsvp.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RsvpRequest {
    @JsonProperty("individual-id")
    @NotNull
    Long individualId;
    boolean confirmed;
    @JsonProperty(value = "menu-type")
    MenuType menuType;
}

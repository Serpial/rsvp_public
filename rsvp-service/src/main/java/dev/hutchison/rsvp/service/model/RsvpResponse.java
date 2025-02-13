package dev.hutchison.rsvp.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RsvpResponse(
        @JsonProperty("individual-id") long individualId,
        String name,
        boolean confirmed,
        @JsonProperty("menu-type") MenuType menuType,
        String updated
) {
}

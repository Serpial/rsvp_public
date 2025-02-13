package dev.hutchison.rsvp.service.config.properties;

import lombok.Data;

@SuppressWarnings("ClassCanBeRecord")
@Data
public final class TokenType {
    private final Lifetime lifetime;
}

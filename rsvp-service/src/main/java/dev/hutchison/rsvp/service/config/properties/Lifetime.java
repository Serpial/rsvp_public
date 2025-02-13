package dev.hutchison.rsvp.service.config.properties;

import lombok.Data;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@SuppressWarnings("ClassCanBeRecord")
@Data
public final class Lifetime {
    private final Long amount;
    private final ChronoUnit unit;

    public Duration getDuration() {
        return Duration.of(amount, unit);
    }
}

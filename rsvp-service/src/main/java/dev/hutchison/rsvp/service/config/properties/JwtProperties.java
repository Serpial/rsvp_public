package dev.hutchison.rsvp.service.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "security.jwt")
public final class JwtProperties {
    private final String secretKey;
    private final TokenType access;
    private final TokenType refresh;
}
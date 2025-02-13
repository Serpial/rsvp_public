package dev.hutchison.rsvp.service.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import dev.hutchison.rsvp.service.config.properties.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtIssuer {
    private final JwtProperties properties;

    public String issue(Long userId, String wordSet, List<String> roles, Duration lifetime) {
        return JWT.create()
                .withSubject(String.valueOf(wordSet))
                .withExpiresAt(Instant.now().plus(lifetime))
                .withClaim("u", userId)
                .withClaim("r", roles)
                .sign(Algorithm.HMAC256(properties.getSecretKey()));
    }

    public String issue(String wordSet, Duration lifetime) {
        return JWT.create()
                .withSubject(String.valueOf(wordSet))
                .withExpiresAt(Instant.now().plus(lifetime))
                .sign(Algorithm.HMAC256(properties.getSecretKey()));
    }
}

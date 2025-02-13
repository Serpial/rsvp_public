package dev.hutchison.rsvp.service.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.hutchison.rsvp.service.config.properties.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtDecoder {
    private static final Logger logger = LoggerFactory.getLogger(JwtDecoder.class);
    private final JwtProperties properties;

    public Optional<DecodedJWT> tryDecode(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(properties.getSecretKey());
            JWTVerifier build = JWT.require(algorithm).build();
            return Optional.of(build.verify(token));
        } catch (JWTVerificationException e) {
            logger.info("Exception thrown when verifying JWT token: {}, {}, token: {}", e.getClass().getName(), e.getMessage(), token);
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

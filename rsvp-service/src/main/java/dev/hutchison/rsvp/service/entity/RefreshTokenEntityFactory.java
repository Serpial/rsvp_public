package dev.hutchison.rsvp.service.entity;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.hutchison.rsvp.service.security.JwtDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RefreshTokenEntityFactory {
    private final JwtDecoder jwtDecoder;

    private static Optional<Long> tryGetUserId(DecodedJWT decodedJWT) {
        if (null == decodedJWT) {
            return Optional.empty();
        }

        Claim userIdClaim = decodedJWT.getClaim("u");
        try {
            long userId = userIdClaim.asLong();
            return Optional.of(userId);
        } catch (Exception ignored) {
        }

        return Optional.empty();
    }

    public Optional<RefreshTokenEntity> tryCreate(String token) {
        Optional<DecodedJWT> maybeDecodedJWT = jwtDecoder.tryDecode(token);
        if (maybeDecodedJWT.isEmpty()) {
            return Optional.empty();
        }

        DecodedJWT decodedJWT = maybeDecodedJWT.get();

        RefreshTokenEntity refreshToken = new RefreshTokenEntity();
        refreshToken.setCreatedAt(new Date());
        refreshToken.setExpiresOn(decodedJWT.getExpiresAt());
        refreshToken.setToken(decodedJWT.getToken());
        tryGetUserId(decodedJWT).ifPresent(refreshToken::setUserId);
        return Optional.of(refreshToken);
    }
}

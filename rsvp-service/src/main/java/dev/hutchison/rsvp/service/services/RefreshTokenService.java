package dev.hutchison.rsvp.service.services;

import dev.hutchison.rsvp.service.entity.RefreshTokenEntity;
import dev.hutchison.rsvp.service.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Service to manage centralize managed interactions with the Refresh Token
 * repository.
 */
@Component
@RequiredArgsConstructor
public class RefreshTokenService {
    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenService.class);

    private final RefreshTokenRepository refreshTokenRepository;

    public Optional<RefreshTokenEntity> findByToken(String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken);
    }

    public boolean trySaveToken(RefreshTokenEntity refreshToken) {
        try {
            refreshTokenRepository.save(refreshToken);
            return true;
        } catch (Exception e) {
            logger.error("Error saving refresh token: {}", e.getMessage());
            return false;
        }
    }

    public boolean tryDeleteToken(RefreshTokenEntity refreshToken) {
        try {
            refreshTokenRepository.deleteById(refreshToken.getId());
            return true;
        } catch (Exception e) {
            logger.error("Error deleting refresh token: {}", e.getMessage());
            return false;
        }
    }

    public boolean tryDeleteTokensForUser(long userId) {
        try {
            refreshTokenRepository.deleteByUserId(userId);
            return true;
        } catch (Exception e) {
            logger.error("Error deleting refresh token for user: {}", e.getMessage());
            return false;
        }
    }
}

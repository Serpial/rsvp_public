package dev.hutchison.rsvp.service.services;

import com.auth0.jwt.interfaces.DecodedJWT;
import dev.hutchison.rsvp.service.config.properties.JwtProperties;
import dev.hutchison.rsvp.service.entity.RefreshTokenEntity;
import dev.hutchison.rsvp.service.entity.RefreshTokenEntityFactory;
import dev.hutchison.rsvp.service.model.LoginResponse;
import dev.hutchison.rsvp.service.security.JwtIssuer;
import dev.hutchison.rsvp.service.security.UserPrincipal;
import dev.hutchison.rsvp.service.security.WordSetAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final JwtIssuer jwtIssuer;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenEntityFactory refreshTokenEntityFactory;
    private final JwtProperties jwtProperties;

    public Optional<LoginResponse> tryLogin(String wordSet) {
        WordSetAuthenticationToken wordSetAuthenticationToken = new WordSetAuthenticationToken(wordSet);
        Authentication authentication = authenticationManager.authenticate(wordSetAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var principal = (UserPrincipal) authentication.getPrincipal();

        List<String> roles = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        Duration accessExpiration = jwtProperties.getAccess().getLifetime().getDuration();
        String accessToken = jwtIssuer.issue(principal.getUserId(), principal.getWordSet(), roles, accessExpiration);
        Duration refreshExpiration = jwtProperties.getRefresh().getLifetime().getDuration();
        String refreshToken = jwtIssuer.issue(principal.getUserId(), principal.getWordSet(), roles, refreshExpiration);

        Optional<RefreshTokenEntity> maybeRefreshTokenEntity = refreshTokenEntityFactory.tryCreate(refreshToken);
        if (maybeRefreshTokenEntity.isEmpty()) {
            return Optional.empty();
        }

        RefreshTokenEntity refreshTokenEntity = maybeRefreshTokenEntity.get();
        if (!refreshTokenService.trySaveToken(refreshTokenEntity)) {
            return Optional.empty();
        }

        logger.info("Saved token for user: {}", refreshTokenEntity.getUserId());
        return Optional.of(LoginResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build());
    }

    public boolean tryLogout(long userId) {
        return refreshTokenService.tryDeleteTokensForUser(userId);
    }

    public Optional<LoginResponse> tryRefreshAccessToken(DecodedJWT decodedRefreshToken) {
        if (null == decodedRefreshToken) {
            return Optional.empty();
        }

        // Verify token exists
        Optional<RefreshTokenEntity> maybeRetrievedRefreshToken = refreshTokenService.findByToken(decodedRefreshToken.getToken());
        if (maybeRetrievedRefreshToken.isEmpty()) {
            return Optional.empty();
        }

        // Delete consumed token
        RefreshTokenEntity retrievedRefreshToken = maybeRetrievedRefreshToken.get();
        if (refreshTokenService.tryDeleteToken(retrievedRefreshToken)) {
            logger.info("A token was consumed for user: {}", retrievedRefreshToken.getUserId());
        }

        // Generate new access and refresh token
        return tryLogin(decodedRefreshToken.getSubject());
    }
}

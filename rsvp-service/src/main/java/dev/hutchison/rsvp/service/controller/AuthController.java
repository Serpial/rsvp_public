package dev.hutchison.rsvp.service.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Authenticator;
import dev.hutchison.rsvp.service.model.LoginRequest;
import dev.hutchison.rsvp.service.model.LoginResponse;
import dev.hutchison.rsvp.service.security.JwtDecoder;
import dev.hutchison.rsvp.service.security.UserPrincipal;
import dev.hutchison.rsvp.service.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;
    private final JwtDecoder jwtDecoder;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static ResponseEntity<LoginResponse> buildUnauthorizedLoginResponse() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Validated LoginRequest request) {
        String wordSet = request.getWordSet();
        logger.info("Attempting login with user: \"{}\"", wordSet);

        Optional<LoginResponse> maybeLoginResponse = authService.tryLogin(wordSet);
        return maybeLoginResponse.map(ResponseEntity::ok).orElseGet(AuthController::buildUnauthorizedLoginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (authService.tryLogout(userPrincipal.getUserId())) {
            return new ResponseEntity<Authenticator.Success>(HttpStatus.OK);
        } else {
            return new ResponseEntity<Authenticator.Failure>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (null == authorization || authorization.isEmpty()) {
            logger.error("Authorization header not set");
            return buildUnauthorizedLoginResponse();
        }

        String givenRefreshToken = authorization.substring(authorization.indexOf(" ") + 1);
        Optional<DecodedJWT> maybeDecodedRefreshToken = jwtDecoder.tryDecode(givenRefreshToken);
        if (maybeDecodedRefreshToken.isEmpty()) {
            logger.error("Could not decode refresh token");
            return buildUnauthorizedLoginResponse();
        }

        Supplier<ResponseEntity<LoginResponse>> onError = () -> {
            logger.error("Could not re-issue access token");
            return buildUnauthorizedLoginResponse();
        };

        DecodedJWT decodedRefreshToken = maybeDecodedRefreshToken.get();
        Optional<LoginResponse> maybeLoginResponse = authService.tryRefreshAccessToken(decodedRefreshToken);
        return maybeLoginResponse.map(ResponseEntity::ok).orElseGet(onError);
    }
}

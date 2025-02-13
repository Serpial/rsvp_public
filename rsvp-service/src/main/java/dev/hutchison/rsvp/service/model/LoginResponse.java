package dev.hutchison.rsvp.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@SuppressWarnings("unused")
@Data
@Builder
public class LoginResponse {
    @JsonProperty("access_token")
    private final String accessToken;

    @JsonProperty("refresh_token")
    private final String refreshToken;
}

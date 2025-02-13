package dev.hutchison.rsvp.service.controller;

import dev.hutchison.rsvp.service.AppTestConfiguration;
import dev.hutchison.rsvp.service.config.properties.JwtProperties;
import dev.hutchison.rsvp.service.entity.GuestEntity;
import dev.hutchison.rsvp.service.entity.RefreshTokenEntity;
import dev.hutchison.rsvp.service.security.GuestDetailService;
import dev.hutchison.rsvp.service.security.JwtIssuer;
import dev.hutchison.rsvp.service.services.GuestService;
import dev.hutchison.rsvp.service.services.RefreshTokenService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Import({AppTestConfiguration.class, AuthControllerTestConfiguration.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest {
    @SuppressWarnings("unused")
    @Autowired
    private MockMvc api;

    @SuppressWarnings("unused")
    @Autowired
    private JwtProperties jwtProperties;

    @Test
    void refresh_notLoggedIn_shouldNotSeeSecuredEndpoint() throws Exception {
        api.perform(post("/api/auth/refresh"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void refresh_loggedIn_shouldSeeSecuredEndpoint() throws Exception {
        JwtIssuer issuer = new JwtIssuer(jwtProperties);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", issuer.issue("sample-sample-sample", Duration.ofDays(1L)));

        MockHttpServletRequestBuilder post = post("/api/auth/refresh");
        post.headers(headers);
        api.perform(post).andExpect(status().isOk());
    }
}

@SuppressWarnings("unused")
@TestConfiguration
class AuthControllerTestConfiguration {
    @Bean
    GuestDetailService guestDetailService() {
        GuestService mockService = Mockito.mock(GuestService.class);
        GuestEntity value = new GuestEntity();
        value.setNames(List.of("John", "Jane"));
        value.setId(1L);
        value.setWordSet("sample-sample-sample");
        Mockito.doReturn(Optional.of(value)).when(mockService).findByWordSet(anyString());
        return new GuestDetailService(mockService);
    }

    @Bean
    RefreshTokenService refreshTokenService() {
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setId("1");
        refreshTokenEntity.setUserId(1L);
        refreshTokenEntity.setToken("asdf");
        refreshTokenEntity.setCreatedAt(new Date());
        refreshTokenEntity.setExpiresOn(Date.from(Instant.now().plus(Duration.ofHours(1L))));

        RefreshTokenService refreshTokenService = Mockito.mock(RefreshTokenService.class);
        Mockito.doReturn(Optional.of(refreshTokenEntity)).when(refreshTokenService).findByToken(anyString());
        Mockito.doReturn(true).when(refreshTokenService).tryDeleteToken(any());
        Mockito.doReturn(true).when(refreshTokenService).trySaveToken(any());
        return refreshTokenService;
    }
}
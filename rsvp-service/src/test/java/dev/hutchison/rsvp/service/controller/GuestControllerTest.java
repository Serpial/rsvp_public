package dev.hutchison.rsvp.service.controller;

import dev.hutchison.rsvp.service.AppTestConfiguration;
import dev.hutchison.rsvp.service.entity.GuestEntity;
import dev.hutchison.rsvp.service.services.GuestService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Nested
@SuppressWarnings("unused")
@AutoConfigureMockMvc
@Import({AppTestConfiguration.class, GuestControllerTestConfiguration.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GuestControllerTest {
    @Autowired
    private MockMvc api;

    @Test
    @WithMockUser
    void names_loggedIn_returnNames() {
        try {
            api.perform(get("/api/guest/names"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("{\"names\":[\"John\",\"Jane\"]}"));
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void names_notLoggedIn_returnUnauthorized() {
        try {
            api.perform(get("/api/guest/names"))
                    .andExpect(status().isUnauthorized());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @WithMockUser
    void wordSet_loggedIn_returnWordSet() {
        try {
            api.perform(get("/api/guest/word-set"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("{\"word-set\":\"sample-sample-sample\"}"));
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void wordSet_notLoggedIn_returnUnauthorized() {
        try {
            api.perform(get("/api/guest/word-set"))
                    .andExpect(status().isUnauthorized());
        } catch (Exception e) {
            fail(e);
        }
    }
}

@SuppressWarnings("unused")
@TestConfiguration
class GuestControllerTestConfiguration {
    private static final String[] TEST_NAMES = {"John", "Jane"};

    @Bean
    GuestService guestService() {
        var guestService = Mockito.mock(GuestService.class);
        var fakeGuest = new GuestEntity();
        fakeGuest.setWordSet("sample-sample-sample");
        fakeGuest.setNames(Arrays.stream(TEST_NAMES).toList());
        Mockito.doReturn(Optional.of(fakeGuest)).when(guestService).findById(ArgumentMatchers.anyLong());
        return guestService;
    }
}

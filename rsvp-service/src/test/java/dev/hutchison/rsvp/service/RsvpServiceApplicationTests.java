package dev.hutchison.rsvp.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(AppTestConfiguration.class)
class RsvpServiceApplicationTests {
    @Test
    void contextLoads() {
    }
}

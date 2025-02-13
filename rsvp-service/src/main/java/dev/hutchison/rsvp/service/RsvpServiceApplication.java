package dev.hutchison.rsvp.service;

import dev.hutchison.rsvp.service.config.properties.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class RsvpServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RsvpServiceApplication.class, args);
    }

}

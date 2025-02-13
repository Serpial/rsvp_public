package dev.hutchison.rsvp.service.security;

import dev.hutchison.rsvp.service.Constants;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class WordSetAuthenticationToken extends UsernamePasswordAuthenticationToken {
    public WordSetAuthenticationToken(Object principal) {
        super(principal, Constants.EMPTY_STRING);
    }
}

package dev.hutchison.rsvp.service.security;

import dev.hutchison.rsvp.service.Constants;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class UserPrincipleAuthenticationToken extends AbstractAuthenticationToken {
    private final UserPrincipal userPrinciple;

    public UserPrincipleAuthenticationToken(UserPrincipal userPrinciple) {
        super(userPrinciple.getAuthorities());
        this.userPrinciple = userPrinciple;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return Constants.EMPTY_STRING;
    }

    @Override
    public UserPrincipal getPrincipal() {
        return userPrinciple;
    }
}

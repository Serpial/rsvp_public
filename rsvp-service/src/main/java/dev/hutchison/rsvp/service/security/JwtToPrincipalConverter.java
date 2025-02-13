package dev.hutchison.rsvp.service.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class JwtToPrincipalConverter {
    public UserPrincipal convert(DecodedJWT decodedJWT) {
        return UserPrincipal.builder()
                .wordSet(decodedJWT.getSubject())
                .userId(decodedJWT.getClaim("u").asLong())
                .authorities(extractAuthoritiesFromClaim(decodedJWT))
                .build();
    }

    private Collection<? extends GrantedAuthority> extractAuthoritiesFromClaim(DecodedJWT decodedJWT) {
        var claim = decodedJWT.getClaim("a");
        if (claim.isNull() || claim.isMissing()) {
            return List.of();
        }

        return claim.asList(SimpleGrantedAuthority.class);
    }
}

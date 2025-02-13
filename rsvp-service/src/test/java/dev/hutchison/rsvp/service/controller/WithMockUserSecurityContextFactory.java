package dev.hutchison.rsvp.service.controller;

import dev.hutchison.rsvp.service.security.UserPrincipal;
import dev.hutchison.rsvp.service.security.UserPrincipleAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;

public class WithMockUserSecurityContextFactory implements WithSecurityContextFactory<WithMockUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockUser annotation) {
        var authorities = Arrays.stream(annotation.authorities()).map(SimpleGrantedAuthority::new).toList();
        var principle = UserPrincipal.builder()
                .userId(annotation.userId())
                .wordSet("sample-sample-sample")
                .authorities(authorities)
                .build();
        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new UserPrincipleAuthenticationToken(principle));
        return context;
    }
}

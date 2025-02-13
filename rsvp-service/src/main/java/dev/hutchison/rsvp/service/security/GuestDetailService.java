package dev.hutchison.rsvp.service.security;

import dev.hutchison.rsvp.service.services.GuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GuestDetailService implements UserDetailsService {
    private final GuestService guestService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var guest = guestService.findByWordSet(username).orElseThrow();
        return UserPrincipal.builder()
                .userId(guest.getId())
                .wordSet(guest.getWordSet())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_USER")))
                .build();
    }
}

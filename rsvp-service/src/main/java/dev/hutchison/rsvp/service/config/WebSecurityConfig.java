package dev.hutchison.rsvp.service.config;

import dev.hutchison.rsvp.service.Constants;
import dev.hutchison.rsvp.service.security.GuestDetailService;
import dev.hutchison.rsvp.service.security.JwtAuthenticationFilter;
import dev.hutchison.rsvp.service.security.UnauthorizedHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@SuppressWarnings("unused")
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    private static PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return Constants.EMPTY_STRING;
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return true;
            }
        };
    }

    @Bean("allowedOrigins")
    @Profile("!local")
    public AllowedOrigins allowedOrigins() {
        return new AllowedOrigins(List.of(
                "https://rsvp-ui.onrender.com",
                "https://rsvp-ui-test.onrender.com",
                "https://test.paulandlois.uk",
                "https://join.paulandlois.uk"));
    }

    @Bean("allowedOrigins")
    @Profile("local")
    public AllowedOrigins allowedOriginsLocal() {
        return new AllowedOrigins(List.of(
                "http://localhost:3000", "http://localhost:5173"));
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(@Qualifier("allowedOrigins") AllowedOrigins allowedOrigins) {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(allowedOrigins.origins());
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "OPTIONS"));
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public SecurityFilterChain applicationSecurity(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            HttpSecurity httpSecurity,
            UnauthorizedHandler unauthorizedHandler,
            CorsConfigurationSource corsConfigurationSource) throws Exception {

        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource))
                .exceptionHandling(exceptionHandlingConfigurer -> exceptionHandlingConfigurer.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(configure -> configure.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .securityMatcher("/**")
                .authorizeHttpRequests(registry -> registry
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/refresh").permitAll()
                        .anyRequest().authenticated()
                ).build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, GuestDetailService guestDetailService, PasswordEncoder passwordEncoder) throws Exception {
        var builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(guestDetailService)
                .passwordEncoder(passwordEncoder);
        return builder.build();
    }
}

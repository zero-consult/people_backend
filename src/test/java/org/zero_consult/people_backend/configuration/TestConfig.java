package org.zero_consult.people_backend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zero_consult.people_backend.security.JwtAuthorizationFilter;
import org.zero_consult.people_backend.security.JwtUtil;
import org.zero_consult.people_backend.security.MockJwtAuthorizationFilter;

@Configuration
public class TestConfig {
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter(CustomUserDetailsService customUserDetailsService, JwtUtil jwtUtil) {
        return new MockJwtAuthorizationFilter(customUserDetailsService, jwtUtil);
    }
}

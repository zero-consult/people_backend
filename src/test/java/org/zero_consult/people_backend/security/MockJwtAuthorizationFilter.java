package org.zero_consult.people_backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.zero_consult.people_backend.configuration.CustomUserDetailsService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MockJwtAuthorizationFilter extends JwtAuthorizationFilter {
    public MockJwtAuthorizationFilter(CustomUserDetailsService customUserDetailsService, JwtUtil jwtUtil) {
        super(customUserDetailsService, jwtUtil);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        List<String> roles = new ArrayList<>();
        roles.add("EMPLOYEE");
        roles.add("ADMIN");
        UserDetails userDetails =
                org.springframework.security.core.userdetails.User.builder()
                        .username("test@test.com")
                        .password("test")
                        .roles(roles.toArray(new String[0]))
                        .build();
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}

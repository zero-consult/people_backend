package org.zero_consult.people_backend.configuration;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.zero_consult.people_backend.entities.AppUser;
import org.zero_consult.people_backend.exceptions.EntityNotFoundException;
import org.zero_consult.people_backend.services.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            AppUser appUser = userService.findByEmail(email);
            List<String> roles = new ArrayList<>();
            roles.add("EMPLOYEE");
            if (appUser.isAdmin()) {
                roles.add("ADMIN");
            }
            UserDetails userDetails =
                    org.springframework.security.core.userdetails.User.builder()
                            .username(email)
                            .password(appUser.getPassword())
                            .roles(roles.toArray(new String[0]))
                            .build();
            return userDetails;
        } catch (EntityNotFoundException e) {
            throw new UsernameNotFoundException(email + " not found");
        }
    }
}

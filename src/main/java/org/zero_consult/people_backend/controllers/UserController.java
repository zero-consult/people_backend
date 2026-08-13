package org.zero_consult.people_backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.zero_consult.idl.api.UserApi;
import org.zero_consult.idl.model.LoginInfo;
import org.zero_consult.idl.model.UserWithToken;
import org.zero_consult.people_backend.configuration.CustomProperties;
import org.zero_consult.people_backend.entities.AppUser;
import org.zero_consult.people_backend.exceptions.EntityNotFoundException;
import org.zero_consult.people_backend.mappers.EmployeeMapper;
import org.zero_consult.people_backend.security.JwtUtil;
import org.zero_consult.people_backend.services.UserService;

@CrossOrigin(origins = {
        "http://localhost",
        "http://dev.localhost",
        "http://localhost:5173",
        "http://localhost:5174",
        "http://localhost:5175",
        "http://invoices.localhost",
        "http://people.localhost",
        "http://timesheet.localhost",
        "http://invoices.dev.localhost",
        "http://people.dev.localhost",
        "http://timesheet.dev.localhost"
})
@RestController
public class UserController implements UserApi {

    private final CustomProperties customProperties;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(CustomProperties customProperties, AuthenticationManager authenticationManager, UserService userService, JwtUtil jwtUtil) {
        this.customProperties = customProperties;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public ResponseEntity<UserWithToken> login(LoginInfo loginInfo) {
        String email = loginInfo.getEmail() != null ? loginInfo.getEmail() : "";
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email.trim().toLowerCase(), loginInfo.getPassword()));
        AppUser appUser;
        try {
            appUser = userService.findByEmail(authentication.getName());
        } catch (EntityNotFoundException e) {
            throw new BadCredentialsException("Can't login", e);
        }
        return respondWithUserAndToken(appUser);
    }

    private ResponseEntity<UserWithToken> respondWithUserAndToken(
            AppUser appUser) {
        UserWithToken userWithToken = new UserWithToken();
        userWithToken.setToken(jwtUtil.createToken(appUser));
        userWithToken.setAdmin(appUser.isAdmin());
        userWithToken.setUser(EmployeeMapper.toIdl(appUser.getEmployee()));
        return ResponseEntity.ok(userWithToken);
    }
}

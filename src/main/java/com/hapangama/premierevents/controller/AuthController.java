package com.hapangama.premierevents.controller;

import com.hapangama.premierevents.dto.ErrorResponseBody;
import com.hapangama.premierevents.dto.LoginRequestBody;
import com.hapangama.premierevents.entity.AuthRequest;
import com.hapangama.premierevents.entity.UserInfo;
import com.hapangama.premierevents.service.JwtService;
import com.hapangama.premierevents.service.UserInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;
    private final UserInfoService userInfoService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateAndGetToken(@Valid @RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            UserInfo userInfo = userInfoService.loadUserInfoByEmail(authRequest.getEmail());
            LoginRequestBody loginRequestBody = new LoginRequestBody();
            loginRequestBody.setEmail(userInfo.getEmail());
            loginRequestBody.setUserId(userInfo.getId());
            loginRequestBody.setToken(jwtService.generateToken(authRequest.getEmail()));
            return new ResponseEntity<>(loginRequestBody, HttpStatus.OK);
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
    }
}

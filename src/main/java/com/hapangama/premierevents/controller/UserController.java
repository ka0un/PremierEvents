package com.hapangama.premierevents.controller;


import com.hapangama.premierevents.dto.ErrorResponseBody;
import com.hapangama.premierevents.entity.UserInfo;
import com.hapangama.premierevents.service.UserInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserInfoService service;

    @PostMapping("/register")
    public ResponseEntity<?> addNewUser(@Valid @RequestBody UserInfo userInfo) {

        if (service.loadUserByUsername(userInfo.getEmail()) != null) {
            return new ResponseEntity<>(new ErrorResponseBody(400, "Bad Request", "User already exists"), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(service.addUser(userInfo), HttpStatus.CREATED);
    }

}

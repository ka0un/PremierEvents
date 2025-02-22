package com.hapangama.premierevents.service;

import com.hapangama.premierevents.entity.UserInfo;
import com.hapangama.premierevents.repository.UserInfoRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoService implements UserDetailsService {

    private final UserInfoRepository repository;
    private final PasswordEncoder encoder;

    public UserInfoService(UserInfoRepository repository, @Lazy PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> userDetail = repository.findByEmail(username);
        return userDetail.map(UserInfoDetails::new).orElse(null);
    }

    public String addUser(UserInfo userInfo) {

        userInfo.setEmail(userInfo.getEmail());
        userInfo.setName(userInfo.getName());
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        return "User Added Successfully";
    }

    public UserInfo loadUserInfoByEmail(String email) {
        return repository.findByEmail(email).orElse(null);
    }
}
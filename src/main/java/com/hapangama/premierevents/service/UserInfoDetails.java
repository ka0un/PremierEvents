package com.hapangama.premierevents.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.hapangama.premierevents.entity.UserInfo;

import java.util.Collection;
import java.util.List;

public class UserInfoDetails implements UserDetails {

    private final String name;
    private final String email;
    private final String password;

    public UserInfoDetails(UserInfo userInfo) {
        this.email = userInfo.getEmail();
        this.password = userInfo.getPassword();
        this.name = userInfo.getName();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

}

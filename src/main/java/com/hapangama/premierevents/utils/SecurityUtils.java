package com.hapangama.premierevents.utils;

import com.hapangama.premierevents.entity.UserInfo;
import com.hapangama.premierevents.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SecurityUtils {

    private final UserInfoService userInfoService;

    public UserInfo getUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }

        return userInfoService.loadUserInfoByEmail(authentication.getName());
    }

}

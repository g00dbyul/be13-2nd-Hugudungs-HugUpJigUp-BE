package com.hugudungs.hugupjigup.auth.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oauth2User = delegate.loadUser(userRequest);

        System.out.println(oauth2User);

        // 사용자 정보 추출
        String email = oauth2User.getAttribute("email");

        // DB에서 사용자 존재 여부 확인 후 신규 가입 처리 또는 기존 회원 정보 업데이트
        return oauth2User;
    }
}

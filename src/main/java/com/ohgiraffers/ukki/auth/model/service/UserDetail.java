package com.ohgiraffers.ukki.auth.model.service;

import com.ohgiraffers.ukki.auth.model.dao.AuthMapper;
import com.ohgiraffers.ukki.auth.model.dto.AuthDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;

@Service
public class UserDetail implements UserDetailsService {

    private final AuthMapper authMapper;

    @Autowired
    public UserDetail(AuthMapper authMapper) {
        this.authMapper = authMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        // userId로 사용자 정보를 DB에서 조회
        AuthDTO user = authMapper.getUserByUserId(userId);

        // 사용자가 존재하지 않으면 예외 처리
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + userId);
        }

        // Spring Security에서 제공하는 User 객체로 변환하여 반환
        return User.builder()
                .username(user.getUserId())  // 사용자 아이디
                .password(user.getUserPass())  // 사용자 비밀번호
                .roles("USER")  // 역할을 필요한대로 설정 (예: "USER" 역할)
                .build();
    }
}

package com.ohgiraffers.ukki.auth.model.service;

import com.ohgiraffers.ukki.auth.model.dao.AuthMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthService {

    private final UserDetail userDetail;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;  // JwtService 주입
    private final AuthMapper authMapper;

    @Autowired
    public AuthService(UserDetail userDetail, BCryptPasswordEncoder passwordEncoder, JwtService jwtService, AuthMapper authMapper) {
        this.userDetail = userDetail;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;  // JwtService 주입
        this.authMapper = authMapper;
    }

    // 사용자 인증
    public boolean authenticateUser(String userId, String userPass) {
        org.springframework.security.core.userdetails.UserDetails userDetails = userDetail.loadUserByUsername(userId);

        if (userDetails == null) {
            return false;
        }

        // 비밀번호 검증
        return passwordEncoder.matches(userPass, userDetails.getPassword());
    }

    // JWT 토큰 생성 (JwtService 사용)
    public String createToken(String userId) {
        return jwtService.createToken(userId);  // JwtService를 사용하여 토큰 생성
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        return jwtService.validateToken(token);
    }

    // 토큰에서 사용자 아이디 추출
    public String getUserIdFromToken(String token) {
        return jwtService.getUserIdFromToken(token);
    }

    public int isUserIdValid(String userId) {
        return authMapper.confirmUserId(userId);
    }
}

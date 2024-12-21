package com.ohgiraffers.ukki.auth.model.service;

import com.ohgiraffers.ukki.auth.model.dao.AuthMapper;
import com.ohgiraffers.ukki.auth.model.dto.AuthDTO;
import com.ohgiraffers.ukki.common.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

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
        try {
            AuthDTO user = authMapper.getUserByUserId(userId);

            if (user == null) {
                return false;
            }

            return passwordEncoder.matches(userPass, user.getUserPass());
        } catch (Exception e) {
            throw new RuntimeException("비밀번호 인증 과정에서 오류가 발생했습니다.", e);
        }
    }

    // JWT 토큰 생성 (JwtService 사용)
    public String createToken(String userId, UserRole userRole) {
        return jwtService.createToken(userId, userRole);  // JwtService를 사용하여 토큰 생성
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        return jwtService.validateToken(token);
    }

    // 토큰에서 사용자 아이디와 userRole 추출
    public Map<String, Object> getUserInfoFromToken(String token) {
        return jwtService.getUserInfoFromToken(token);
    }

    public int isUserIdValid(String userId) {
        return authMapper.confirmUserId(userId);
    }
}

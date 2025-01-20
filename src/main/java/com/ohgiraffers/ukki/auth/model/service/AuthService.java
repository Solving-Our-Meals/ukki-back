package com.ohgiraffers.ukki.auth.model.service;

import com.ohgiraffers.ukki.auth.model.dao.AuthMapper;
import com.ohgiraffers.ukki.auth.model.dto.AuthDTO;
import com.ohgiraffers.ukki.auth.model.dto.ForJwtDTO;
import com.ohgiraffers.ukki.common.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
            throw new RuntimeException("ⓘ 비밀번호 인증 과정에서 오류가 발생했습니다.", e);
        }
    }
    
    // 순환때문에 여기다가 한번더 돌리는 역할로 생성함

    // JWT 토큰 생성 (JwtService 사용)
    public String createToken(String userId, UserRole userRole, int userNo) {
        return jwtService.createToken(userId, userRole, userNo);
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

    // 리프토
    public String createRefreshToken(String userId) {
        return jwtService.createRefreshToken(userId);
    }

    // 리프토
    public boolean validateRefreshToken(String refreshToken) {
        return jwtService.validateRefreshToken(refreshToken);
    }

    public String getRefresh(HttpServletRequest request) {
        return jwtService.getRefresh(request);
    }

    public ForJwtDTO findUserRoleAndUserNoById(String userId) {
        return authMapper.findUserRoleAndUserNoById(userId);
    }


    public Map<String, Object> getUserInfoFromRefreshToken(String refreshToken) {
        return jwtService.getUserInfoFromRefreshToken(refreshToken);
    }

    public AuthDTO findUserById(String userId) {
        return authMapper.findUserById(userId);
    }
}

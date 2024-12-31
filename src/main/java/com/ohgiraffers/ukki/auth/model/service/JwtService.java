package com.ohgiraffers.ukki.auth.model.service;

import com.ohgiraffers.ukki.common.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    // 보안을 위해 application.yml의 jwt.secret을 받아서 사용
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    // JWT 토큰 생성용 (엑세스 토큰)
    public String createToken(String userId, UserRole userRole, int userNo) {
        System.out.println("userId: " + userId);  // 정상
        System.out.println("userRole: " + userRole);  // null
        System.out.println("userNo: " + userNo);  // 0

        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("userRole", userRole);
        claims.put("userNo", userNo);
        Date now = new Date();
        Date expired = new Date(now.getTime() + 3600000); // 1시간 설정

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expired)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    // 필터 측 토큰 유효성 검사 부분 // 서명확인, 변조 확인 목적성
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY) // 비밀키 bearer뒤에 올 키에 대한 값 확인용인듯? 내가 application.yml에 저장한 값이 있는지 검증하는 것으로 보임 - 서명 검증 키
                    .build() // 새로운 버전에서는 parerbuilder()를 사용하면서 build()가 추가된듯
                    .parseClaimsJws(token); // JWT 파싱 및 서명 검증 -> 서명이 검증되지 않거나 변조되 경우 예외 발생
            return true;
        } catch (Exception e) {
            return false; // 서명 검즘이 안되면 false 반환하겠다.
        }
    }

    // 토큰에서 사용자 아이디와 userRole 추출용도
    public Map<String, Object> getUserInfoFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String userId = claims.getSubject(); // 사용자 아이디
        String userRoleString = (String) claims.get("userRole"); // 사용자 역할 (userRole는 claims에 "userRole" 저장)
        UserRole userRole = UserRole.valueOf(userRoleString);
        int userNo = (int) claims.get("userNo");

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userId", userId);
        userInfo.put("userRole", userRole);
        userInfo.put("userNo", userNo);

        System.out.println("Decoded JWT: " + userInfo);

        return userInfo;
    }

    // 리프레시 토큰에 대한 부분이 없어 추가해야 한다.

    // 리프토 생성
    public String createRefreshToken(String userId) {
        Claims claims = Jwts.claims().setSubject(userId);
        Date now = new Date();
        Date expired = new Date(now.getTime() + 604800000); // 7일 설정 (리프레시 토큰 유효 기간)

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expired)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    // 리프토 검증
    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 리프토 중복 확인
    public String getRefresh(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue(); // 원래 있던 리프토 반환해보기
                }
            }
        }
        return null;
    }
}

package com.ohgiraffers.ukki.auth.model.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    // 보안을 위해 application.yml의 jwt.secret을 받아서 사용
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    // JWT 토큰 생성용 (엑세스 토큰)
    public String createToken(String userId) {
        Claims claims = Jwts.claims().setSubject(userId);
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

    // 토큰에서 사용자 아이디 추출용도
    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY) // 비밀키로 검증한번하고
                .build() // 마찬가지로 넣어야할거같아서 넣었는데 객체 생성을 위해서 쓴다고하는데 이걸 안쓰면 아래 검증이 안돼서 쓴다.
                .parseClaimsJws(token) // jwt 파싱 및 서명 검증
                .getBody(); // jwt 본문(payload) get

        return claims.getSubject(); // 본문에서 사용자 아이디 반환
    }
    // 여기까지가 엑세스 토큰?
}

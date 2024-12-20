package com.ohgiraffers.ukki.auth.controller;

import com.ohgiraffers.ukki.auth.model.dto.AuthDTO;
import com.ohgiraffers.ukki.auth.model.dto.JwtTokenDTO;
import com.ohgiraffers.ukki.auth.model.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody AuthDTO authDTO) {
        boolean isAuthenticated = authService.authenticateUser(authDTO.getUserId(), authDTO.getUserPass());

        if (isAuthenticated) {
            // 인증 성공 시 JWT 토큰 생성
            String token = authService.createToken(authDTO.getUserId());

            // LoginResponse DTO를 사용하여 응답
            JwtTokenDTO response = new JwtTokenDTO("로그인 성공!", token);
            return ResponseEntity.ok(response);
        }

        // 인증 실패
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("아이디 또는 비밀번호가 잘못되었습니다.");
    }
}

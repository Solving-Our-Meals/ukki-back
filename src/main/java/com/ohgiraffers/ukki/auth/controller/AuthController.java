package com.ohgiraffers.ukki.auth.controller;

import com.ohgiraffers.ukki.auth.model.dto.AuthDTO;
import com.ohgiraffers.ukki.auth.model.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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

    @PostMapping("/auth/login/step-one")
    public ResponseEntity<?> checkUserId(@RequestBody AuthDTO authDTO) {
        try {
            int isUserIdValid = authService.isUserIdValid(authDTO.getUserId());
            if (isUserIdValid == 0) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("isValid", false, "message", "아이디가 존재하지 않습니다."));
            }
            return ResponseEntity.ok(Map.of("isValid", true, "message", "아이디 확인 완료"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("isValid", false, "message", "서버 오류가 발생했습니다."));
        }
    }

    @PostMapping("/auth/login/step-two")
    public ResponseEntity<?> authenticatePassword(@RequestBody AuthDTO authDTO, HttpServletResponse response) {
        try {
            boolean isPasswordValid = authService.authenticateUser(authDTO.getUserId(), authDTO.getUserPass());
            if (!isPasswordValid) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "비밀번호가 잘못되었습니다."));
            }

            String token = authService.createToken(authDTO.getUserId(), authDTO.getUserRole());

            // 토큰 쿠키 저장용
            Cookie cookie = new Cookie("authToken", token);
            cookie.setHttpOnly(false); // 이것도 배포 전에 false
            cookie.setSecure(false); // HTTPS에서만 전송되게 설정 -> 보안땜시 cookie.setSecure(false);  // 배포전엔 false 사용
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60); // 유효기간 -> 1 시간 -> 24시간 : (60 * 60 * 24)
            response.addCookie(cookie);

            return ResponseEntity.ok(Map.of("success", true, "message", "로그인 성공 !", "token", token));
        } catch (Exception e) {
            e.printStackTrace(); // 에러가 뭔지 전혀 모르겠으면 사용 -> HS512가 512bit수준의 SECRET_KEY를 원하는데 내가 너무 짧게 설정해서 오류가난거임 -> 해결완료
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "서버 오류가 발생했습니다."));
        }
    }

}

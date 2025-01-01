package com.ohgiraffers.ukki.auth.controller;

import com.ohgiraffers.ukki.auth.model.dto.AuthDTO;
import com.ohgiraffers.ukki.auth.model.dto.ForJwtDTO;
import com.ohgiraffers.ukki.auth.model.service.AuthService;
import com.ohgiraffers.ukki.common.UserRole;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login/step-one")
    public ResponseEntity<?> checkUserId(@RequestBody AuthDTO authDTO) {
        try {
            int isUserIdValid = authService.isUserIdValid(authDTO.getUserId());
            if (isUserIdValid == 0) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("isValid", false, "message", "ⓘ 아이디가 존재하지 않습니다."));
            }
            return ResponseEntity.ok(Map.of("isValid", true, "message", "ⓘ 아이디 확인 완료"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("isValid", false, "message", "ⓘ 서버 오류가 발생했습니다."));
        }
    }

    @PostMapping("/login/step-two")
    public ResponseEntity<?> authenticatePassword(@RequestBody AuthDTO authDTO, HttpServletResponse response, HttpServletRequest request) {
        ForJwtDTO forJwtDTO = authService.findUserRoleAndUserNoById(authDTO.getUserId());
        System.out.println(forJwtDTO.getUserRole());
        System.out.println(forJwtDTO.getUserNo());
        try {
            boolean isPasswordValid = authService.authenticateUser(authDTO.getUserId(), authDTO.getUserPass());

            if (!isPasswordValid) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "ⓘ 비밀번호가 잘못되었습니다."));
            }

            String token = authService.createToken(authDTO.getUserId(), authDTO.getUserRole(), authDTO.getUserNo());

            String existingRefreshToken = authService.getRefresh(request);

            String refreshToken;
            if (existingRefreshToken != null) {
                refreshToken = existingRefreshToken;
            } else {
                refreshToken = authService.createRefreshToken(authDTO.getUserId());
            }

            // 토큰 쿠키 저장용
            Cookie cookie = new Cookie("authToken", token);
            cookie.setHttpOnly(false); // 이것도 배포 전에 false
            cookie.setSecure(false); // HTTPS에서만 전송되게 설정 -> 보안땜시 cookie.setSecure(false);  // 배포전엔 false 사용
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60); // 유효기간 -> 1 시간 -> 24시간 : (60 * 60 * 24)
            response.addCookie(cookie);

            // 리프토 부분
            Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
            refreshCookie.setHttpOnly(false); // 리프레시 토큰은 보안을 위해 HttpOnly 설정
            refreshCookie.setSecure(false); // 배포하면 트루
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge(60 * 60 * 24 * 7); // 7일
            response.addCookie(refreshCookie);

            return ResponseEntity.ok(Map.of("success", true, "message", "ⓘ 로그인 성공 !", "token", token));
        } catch (Exception e) {
            e.printStackTrace(); // 에러가 뭔지 전혀 모르겠으면 사용 -> 사용해보고 에러 보니까 HS512가 512bit수준의 SECRET_KEY를 원하는데 내가 너무 짧게 설정해서 오류가난거임 -> yml에서 해결완료
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "ⓘ 서버 오류가 발생했습니다."));
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> requestBody, HttpServletResponse response) {
        String refreshToken = requestBody.get("refreshToken");
        if (refreshToken != null && authService.validateRefreshToken(refreshToken)) {
            Map<String, Object> userInfo = authService.getUserInfoFromToken(refreshToken);
            String userId = (String) userInfo.get("userId");
            int userNo = (int) userInfo.get("userNo");

            String newToken = authService.createToken(userId, UserRole.valueOf((String) userInfo.get("userRole")), userNo);

            Cookie newCookie = new Cookie("authToken", newToken);
            newCookie.setHttpOnly(false); // 배포하면 트루
            newCookie.setSecure(false); // 배포하면 트루
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60);
            response.addCookie(newCookie);

            return ResponseEntity.ok(Map.of("token", newToken));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "ⓘ 리프레시 토큰이 유효하지 않습니다."));
        }
    }

    // 로그아웃 관련
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            deleteCookie(request, response, "authToken");
            deleteCookie(request, response, "refreshToken");

            return ResponseEntity.ok(Map.of("success", true, "message", "ⓘ 로그아웃 성공"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "ⓘ 서버 오류가 발생했습니다."));
        }
    }

    private void deleteCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }
        }
    }

}

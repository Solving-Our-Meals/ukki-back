package com.ohgiraffers.ukki.user.controller;

import com.ohgiraffers.ukki.user.model.dto.EmailDTO;
import com.ohgiraffers.ukki.user.model.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/auth")
public class EmailController {

    @Autowired
    private EmailService emailService;

    // 이메일 유효성 검사용
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    @PostMapping("/checkemail")
    public ResponseEntity<Map<String, Object>> checkEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        boolean isDuplicate = emailService.isEmailDuplicate(email); // 중복
        boolean isNoshowLimitExceeded = emailService.isNoshowLimitExceeded(email); // 노쇼

        Map<String, Object> response = new HashMap<>();
        response.put("isDuplicate", isDuplicate);
        response.put("isNoshowLimitExceeded", isNoshowLimitExceeded);

        // 이메일
        if (isDuplicate) {
            response.put("message", "이 이메일은 이미 사용 중입니다."); // 중복
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response); // 409 Conflict
        }

        // 노쇼
        if (isNoshowLimitExceeded) {
            response.put("message", "이 이메일의 노쇼 횟수가 3회 이상입니다. 가입이 불가합니다."); // 노쇼
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response); // 403 Forbidden
        }

        // 정상
        return ResponseEntity.ok(response); // 200 OK
    }

    @PostMapping("/sendemail")
    public ResponseEntity<Map<String, Object>> sendEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        if (!isValidEmail(email)) {
            return ResponseEntity.badRequest().body(Map.of("message", "ⓘ 유효하지 않은 이메일입니다."));
        }

        // 이메일과 인증 번호를 서비스로 보내서 처리
        boolean emailSent = emailService.sendAuthCodeAndSave(email);

        Map<String, Object> response = new HashMap<>();
        response.put("success", emailSent);
        return emailSent ? ResponseEntity.ok(response) : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @PostMapping("/verifycode")
    public ResponseEntity<Map<String, Object>> verifyCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String authCode = request.get("authCode");

        boolean isValidCode = emailService.verifyAuthCode(email, authCode);

        Map<String, Object> response = new HashMap<>();
        response.put("isValid", isValidCode);

        if (isValidCode) {
            return ResponseEntity.ok(response);  // 인증 성공
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);  // 인증 실패
        }
    }
}

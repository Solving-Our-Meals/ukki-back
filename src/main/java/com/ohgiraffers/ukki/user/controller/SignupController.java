package com.ohgiraffers.ukki.user.controller;

import com.ohgiraffers.ukki.user.model.dto.SignupUserDTO;
import com.ohgiraffers.ukki.user.model.service.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class SignupController {

    @Autowired
    private SignupService signupService;

    @PostMapping("/signupid")
    public ResponseEntity<Map<String, Boolean>> signup(@RequestBody SignupUserDTO signupUserDTO) {
        boolean isValid = signupService.signupId(signupUserDTO);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isValid", isValid);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/signuppwd")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkPassword(@RequestBody Map<String, String> request) {
        String password = request.get("userPass");

        String resultMessage = signupService.validatePassword(password);
        if (resultMessage.equals("ⓘ 비밀번호가 유효합니다.")) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("isValid", true, "message", resultMessage));
        } else {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("isValid", false, "message", resultMessage));
        }
    }

    @PostMapping("/signupnickname")
    public ResponseEntity<Map<String, Boolean>> signupNick(@RequestBody SignupUserDTO signupUserDTO) {
        boolean isValid = signupService.signupNickname(signupUserDTO);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isValid", isValid);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    // 마지막 모든 걸 합친 회원가입
    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> realSignup(@RequestBody SignupUserDTO signupUserDTO) {
        boolean isSignupSuccess = signupService.realSignup(signupUserDTO);
        Map<String, Object> response = new HashMap<>();
        if (isSignupSuccess) {
            response.put("success", true);
            response.put("message", "ⓘ 회원가입이 완료되었습니다!");
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } else {
            response.put("success", false);
            response.put("message", "ⓘ 회원가입에 실패했습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }
    }

}
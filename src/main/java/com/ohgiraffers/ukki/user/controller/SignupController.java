package com.ohgiraffers.ukki.user.controller;

import com.ohgiraffers.ukki.user.model.dto.SignupUserDTO;
import com.ohgiraffers.ukki.user.model.service.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class SignupController {

    @Autowired
    private SignupService signupService;

    @PostMapping("/signupid")
    public ResponseEntity<?> duplicateUserName(@RequestBody SignupUserDTO signupUserDTO) {
        boolean isValidId = signupService.isValidId(signupUserDTO.getUserName());
        if (isValidId) {
            return ResponseEntity.badRequest().body("아이디가 이미 존재합니다.");
        }
        return ResponseEntity.ok("아이디 사용 가능");
    }

    @PostMapping("/signupemail")
    public ResponseEntity<?> duplicateUserEmail(@RequestBody SignupUserDTO signupUserDTO) {
        boolean isValidEmail = signupService.isValidEmail(signupUserDTO.getEmail());
        if (isValidEmail) {
            return ResponseEntity.badRequest().body("중복된 이메일입니다.");
        }
        return ResponseEntity.ok("이메일 사용 가능");
    }

    @PostMapping("/signupnick")
    public ResponseEntity<?> duplicateUserNick(@RequestBody SignupUserDTO signupUserDTO) {
        boolean isValidNick = signupService.isValidNick(signupUserDTO.getUserNick());
        if (isValidNick) {
            return ResponseEntity.badRequest().body("닉네임이 이미 존재합니다.");
        }
        return ResponseEntity.ok("닉네임 사용 가능");
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupUserDTO signupUserDTO) {
        signupService.signup(signupUserDTO);
        return ResponseEntity.ok("회원가입 완료");
    }
}

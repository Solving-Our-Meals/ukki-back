package com.ohgiraffers.ukki.user.controller;

import com.ohgiraffers.ukki.user.model.dto.EmailDTO;
import com.ohgiraffers.ukki.user.model.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class EmailController {

    @Autowired
    private EmailService emailService;

    // 이메일 인증 코드 전송
    @PostMapping("/sendemail")
    public boolean sendEmailVerification(@RequestBody EmailDTO emailDTO) {
        String email = emailDTO.getEmail();
        return emailService.sendCode(email);
    }

    // 인증 코드 확인
    @PostMapping("/verifycode")
    public boolean verifyCode(@RequestBody EmailDTO emailDTO) {
        String email = emailDTO.getEmail();
        String code = emailDTO.getCode();
        return emailService.verifyCode(email, code);
    }
}

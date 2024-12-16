package com.ohgiraffers.ukki.user.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class EmailService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    // 이메일 인증 코드 생성 후 전송
    public boolean sendCode(String email) {
        String verificationCode = UUID.randomUUID().toString().substring(0, 6);

        try {

            redisTemplate.opsForValue().set(email, verificationCode, 5, TimeUnit.MINUTES);

            sendEmail(email, verificationCode);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void sendEmail(String toEmail, String verificationCode) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("이메일 인증 코드");
            message.setText("여기에 인증 코드: " + verificationCode + " 를 입력하세요.");

            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("이메일 전송에 실패했습니다: " + e.getMessage(), e);
        }
    }

    public boolean verifyCode(String email, String code) {
        String storedCode = redisTemplate.opsForValue().get(email);

        return storedCode != null && storedCode.equals(code);
    }
}

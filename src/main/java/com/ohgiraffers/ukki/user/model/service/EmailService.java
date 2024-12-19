package com.ohgiraffers.ukki.user.model.service;

import com.ohgiraffers.ukki.user.model.dao.EmailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private EmailMapper emailMapper;

    public boolean isEmailDuplicate(String email) {
        return emailMapper.countByEmail(email) > 0;
    }

    public boolean sendAuthCodeEmail(String email, String authCode) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("이메일 인증 코드");
            message.setText("귀하의 인증 코드: " + authCode);

            mailSender.send(message);  // 이메일 전송
            return true;
        } catch (Exception e) {
            return false;  // 이메일 전송 실패
        }
    }

    // 코드 생성용
    private String generateAuthCode() {
        Random random = new Random();
        StringBuilder authCode = new StringBuilder();
        for (int i = 0; i < 6; i++) {  // 6자리 인증번호 생성
            authCode.append(random.nextInt(10));  // 0~9까지 숫자 생성
        }
        return authCode.toString();
    }

    // 인증코드 redis 저장용
    private void saveAuthCodeToRedis(String email, String authCode) {
        String redisKey = "authCode:" + email;
        redisTemplate.opsForValue().set(redisKey, authCode, 30, TimeUnit.MINUTES); // 30분
    }

    // 이메일 인증 코드 생성 및 전송, Redis 저장
    public boolean sendAuthCodeAndSave(String email) {
        String authCode = generateAuthCode();  // 인증 코드 생성
        saveAuthCodeToRedis(email, authCode);  // Redis에 저장
        return sendAuthCodeEmail(email, authCode);  // 이메일 전송
    }

    public boolean verifyAuthCode(String email, String authCode) {
        String redisKey = "authCode:" + email;
        String storedCode = redisTemplate.opsForValue().get(redisKey);

        return storedCode != null && storedCode.equals(authCode);
    }
}

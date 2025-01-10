package com.ohgiraffers.ukki.user.model.service;

import com.ohgiraffers.ukki.user.model.dao.EmailMapper;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
            // 기존에 쓰던 심플메일은 텍스트만 가능
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);  // true indicates multipart message

            helper.setTo(email);
            helper.setSubject("ukki(우리들의 끼니해결) 인증메일");
            helper.setText("<html><body>" +
                    "<p style='font-weight: bold; font-size: 40px; width:auto;'>우끼 인증번호 : " + authCode + "</p>" +
                    "<img src='cid:logoImage' style='width: 400px; height: auto;' alt='Logo' />" +
                    "</body></html>", true);

            // 랜덤 이미지 정하려고 넣었다. - 이미지 경로 문제있으면 메일 안가니까 조심 -> 이메일 보내기 실패 뜨면 여기부터 확인
            String[] imageFiles = {"images/logo1.png", "images/logo2.png", "images/logo3.png", "images/logo4.png", "images/logo5.png", "images/logo6.png"};
            Random rand = new Random();
            int randomIndex = rand.nextInt(imageFiles.length);
            String selectedImage = imageFiles[randomIndex];

            ClassPathResource resource = new ClassPathResource(selectedImage);
            helper.addInline("logoImage", resource);

            mailSender.send(message);

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

    public boolean isNoshowLimitExceeded(String email) {
        int noshowCount = emailMapper.getNoshowCountByEmail(email);
        return noshowCount >= 3;
    }
}

package com.ohgiraffers.ukki.qr.model.service;

import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import java.util.concurrent.TimeUnit;

@Service
public class QrService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public QrService(@Qualifier("redisTemplate") RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean qrSend(String email,String qrName, byte[] qrCodeByteArray) {
        saveQrCodeToRedis(email, qrName);
        return sendQrCodeEmail(email, qrCodeByteArray);
    }

    // 인증코드 redis 저장용
    private void saveQrCodeToRedis(String email, String qrName) {
//        코드의 만료기간은 해당 예약의 예약시간 + 5분 ->
//                redisTemplate에서 set 어떻게 정할지 생각하자. -> 예약 시간도 받아야겠네
//        예약시간을 Date로 전환시키고 그 Date빼기 현재시간 + 5분
        String redisKey = "qrCode:" + email;
        redisTemplate.opsForValue().set(redisKey, qrName, 30, TimeUnit.MINUTES);
    }

    public boolean sendQrCodeEmail(String email, byte[] qrCodeByteArray) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject("예약 확인 QR");
            helper.setText("<p>예약 확인을 위해 QR코드를 스캔해주세요.</p>", true);
            helper.addAttachment("QRCode.png", new ByteArrayDataSource(qrCodeByteArray, "image/png"));

            mailSender.send(message);  // 이메일 전송
            return true;
        } catch (Exception e) {
            return false;  // 이메일 전송 실패
        }
    }

}

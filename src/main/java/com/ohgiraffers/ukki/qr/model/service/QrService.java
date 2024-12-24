package com.ohgiraffers.ukki.qr.model.service;

import com.ohgiraffers.ukki.qr.model.dao.QrMapper;
import com.ohgiraffers.ukki.qr.model.dto.QrConfirmDTO;
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

    @Autowired
    private QrMapper qrMapper;

    public QrService(@Qualifier("redisTemplate") RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean qrSend(String email,String qrName, byte[] qrCodeByteArray, long keepTime) {
        saveQrCodeToRedis(email, qrName, keepTime);
        return sendQrCodeEmail(email, qrCodeByteArray);
    }

    // 인증코드 redis 저장용
    private void saveQrCodeToRedis(String email, String qrName, long keepTime) {
        String redisKey = "qrCode:" + email;
        redisTemplate.opsForValue().set(redisKey, qrName, keepTime, TimeUnit.MINUTES);
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

    public QrConfirmDTO qrConfirmation(String qr) {
        return qrMapper.qrConfirmation(qr);
    }

    public int qrConfirmSuccess(String qr) {
        return qrMapper.qrConfirmSuccess(qr);
    }
}

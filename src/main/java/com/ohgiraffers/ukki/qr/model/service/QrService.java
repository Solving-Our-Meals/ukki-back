package com.ohgiraffers.ukki.qr.model.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.ohgiraffers.ukki.qr.model.dao.QrMapper;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class QrService {

        private final String SHARED_FOLDER = "\\\\192.168.0.138\\ukki_nas\\qr";
//    private final String SHARED_FOLDER = "C:\\Users\\admin\\Desktop\\ukkiImg";

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

    public int qrConfirmation(String qr, String userId) {
//    qr로 가게 사장 아이디 불러오기
      String storeUserName = qrMapper.resStoreUserName(qr);
        System.out.println(storeUserName);

        if(storeUserName.equals(userId)) {
            return 1;
        }else {
            return 0;
        }
    }

    public String qrCertificate() throws WriterException {
        System.out.println("실행");
        String randomCode = String.valueOf(UUID.randomUUID());
        String qrName = "resQr"+randomCode;


        System.out.println(qrName);
        int width = 200;
        int height = 200;
        String url = "http://localhost:3000/qr/"+qrName;

        String filePath = SHARED_FOLDER + "/"+ qrName+".png";
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, width, height);
        File outputFile = new File(filePath);

//        경로가 존재하는지 파악하기 위함
        File parentDir = outputFile.getParentFile();

        if (!parentDir.exists()) {
            parentDir.mkdirs(); // 경로가 존재하지 않으면 생성
        }

        try(FileOutputStream fos = new FileOutputStream(outputFile)){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "png", baos);
            fos.write(baos.toByteArray());


            return qrName;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void editQrConfirmRes(String qr) {
        Path filePathInquiry = Paths.get(SHARED_FOLDER, qr+".png");

        try {
            System.out.println(filePathInquiry);
            Files.deleteIfExists(filePathInquiry);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        qrMapper.editQrConfirmRes(qr);

    }
}

package com.ohgiraffers.ukki.qr.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.ohgiraffers.ukki.qr.model.dto.QrConfirmDTO;
import com.ohgiraffers.ukki.qr.model.service.QrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class QrController {

    private final QrService qrService;

    @Autowired
    public QrController(QrService qrService){
        this.qrService = qrService;
    }

    @GetMapping("/qr")
    public String qrCertificate() throws WriterException {

//        qrCertificate -> qrName을 만들고 reservation에 저장할 수 있도록
//        string 그리고 이 안에 qrMaker을 만들어서 이메일로 전송할 거다.
//          -> 이를 위해서 해당 회원의 email을 조회해야한다.
//        코드의 만료기간은 해당 예약의 예약시간 + 5분 ->
//        redisTemplate에서 set 어떻게 정할지 생각하자. -> 예약 시간도 받아야겠네
//        예약시간을 Date로 전환시키고 그 Date빼기 현재시간 + 5분
//        null일 때 실패를 반환해야함.

//        예약시간은 2024-12-23 12:30으로 잡아 놓고 12:35분을 계산하여 redis에 보관해보자.
        String resDate = "2024-12-30 12:30";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime resTime = LocalDateTime.parse(resDate, formatter);
        resTime = resTime.plus(5, ChronoUnit.MINUTES);
        LocalDateTime now = LocalDateTime.now();

        long keepTime = ChronoUnit.MINUTES.between(now, resTime);

        System.out.println(keepTime);

// 현재 qr을 보내는 것은 완료 되었다. 그 qr을 통해 인증을 완료할 수 있는 창을 만들고 userNo가지고 db에서 email을 가져올 수 있게 해야겠다.
//        qr을 보내기 위해서 필요한 값은 userEmail, resTime, resNo->코드중복성을 아예 없애기 위해서 필수는 아니다.
        String email = "gudjtr097@gmail.com";
        int resNo = 10;

        System.out.println("실행");
        String randomCode = String.valueOf(UUID.randomUUID());
        String qrName = randomCode.substring(0,10)+resNo;


        System.out.println(qrName);
        int width = 200;
        int height = 200;
        String url = "http://localhost:3000/qr/"+qrName;

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, width, height);
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "png", baos);
            byte[] qrCodeByteArray = baos.toByteArray();

            boolean isQrSend = qrService.qrSend(email, qrName ,qrCodeByteArray, keepTime);
            if(isQrSend){
                return qrName;
            }else {
                return null;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @GetMapping("/qr/{QR}")
    public ResponseEntity<?> qrConfirmation(@PathVariable String QR){
//        QR코드 확인 페이지에서 get요청 오게 되면 예약에 저장되어 있는 가게번호와 예약번호를 반환한다.
        QrConfirmDTO qrConfirmDTO = qrService.qrConfirmation(QR);

        if(qrConfirmDTO!=null) {
            return ResponseEntity.ok(qrConfirmDTO);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"none\"}");
        }
    }

    @PutMapping("/qr/{QR}/confirm")
    public ResponseEntity<?> qrConfirmSuccess(@PathVariable String QR){
        int result = qrService.qrConfirmSuccess(QR);

        Map<String, Object> responseMap = new HashMap<>();
        String message = "";
        if(result>0){
            message="QR 확인에 성공하였습니다..";
            responseMap.put("message", message);
            return ResponseEntity.ok(responseMap);
        }else {
            message="오류로 인해 확인에 실패했습니다.";
            responseMap.put("message", message);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
        }
    }
}


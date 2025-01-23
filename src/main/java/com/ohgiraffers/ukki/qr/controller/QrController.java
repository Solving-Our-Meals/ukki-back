package com.ohgiraffers.ukki.qr.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.ohgiraffers.ukki.auth.model.service.JwtService;
import com.ohgiraffers.ukki.qr.model.service.QrService;
import com.ohgiraffers.ukki.user.model.service.CookieService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class QrController {

    private final QrService qrService;
    private final JwtService jwtService;
    private final CookieService cookieService;

    private final String SHARED_FOLDER = "\\\\192.168.0.138\\ukki_nas\\qr";
//    private final String SHARED_FOLDER = "C:\\Users\\admin\\Desktop\\ukkiImg";

    @Autowired
    public QrController(QrService qrService, JwtService jwtService, CookieService cookieService) {
        this.qrService = qrService;
        this.jwtService = jwtService;
        this.cookieService = cookieService;
    }

    @GetMapping("/qr/{QR}")
    public ResponseEntity<?> qrConfirmation(@PathVariable String QR, HttpServletRequest request){
//        QR코드 확인 페이지에서 get요청 오게 되면 예약에 저장되어 있는 가게번호와 예약번호를 반환한다.
        String jwtToken = cookieService.getJWTCookie(request);

        if (jwtToken == null) {
            throw new IllegalArgumentException("토큰이 일치하지 않음");
        }

        String userId = jwtService.getUserInfoFromTokenId(jwtToken);

        if (userId == null) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        int isConfirm = qrService.qrConfirmation(QR, userId);

        if(isConfirm!=0) {
            qrService.editQrConfirmRes(QR);
            Map<String, Object> data = new HashMap<>();
            data.put("message", "예약 확인에 성공했습니다.");

            return ResponseEntity.ok(data);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"none\"}");
        }
    }

    @GetMapping(value = "{qrName}/api/qrImage")
    public ResponseEntity<Resource> getQrImage(@PathVariable String qrName){

        try {

            System.out.println("QR이미지 왔다");
            System.out.println(qrName);
            Path file = Paths.get(SHARED_FOLDER).resolve(qrName + ".png");
            if(!Files.exists(file)){
                file = Paths.get(SHARED_FOLDER).resolve(qrName + ".jpg");
            }

            // 디버깅 확인
//            System.out.println("프로필 파일 경로 : " + file);
            Resource resource = new UrlResource(file.toUri());

            System.out.println(resource);
            System.out.println(file);
            if(resource.exists() && resource.isReadable()){
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; qrName=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}


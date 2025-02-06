package com.ohgiraffers.ukki.qr.controller;

import com.ohgiraffers.ukki.auth.model.service.JwtService;
import com.ohgiraffers.ukki.qr.model.service.QrService;
import com.ohgiraffers.ukki.user.model.service.CookieService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import com.ohgiraffers.ukki.common.service.GoogleDriveService;
@RestController
public class QrController {

    private final QrService qrService;
    private final JwtService jwtService;
    private final CookieService cookieService;

    private final GoogleDriveService googleDriveService;

    @Autowired
    public QrController(QrService qrService, JwtService jwtService, CookieService cookieService, GoogleDriveService googleDriveService) {
        this.qrService = qrService;
        this.jwtService = jwtService;
        this.cookieService = cookieService;
        this.googleDriveService = googleDriveService;
    }

    @GetMapping("/qr/{resNo}")
    public ResponseEntity<?> qrConfirmation(@PathVariable int resNo, HttpServletRequest request){
//        QR코드 확인 페이지에서 get요청 오게 되면 예약에 저장되어 있는 가게번호와 예약번호를 반환한다.
        String jwtToken = cookieService.getJWTCookie(request);

        if (jwtToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String userId = jwtService.getUserInfoFromTokenId(jwtToken);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        int isConfirm = qrService.qrConfirmation(resNo, userId);

        if(isConfirm!=0) {
            String QR = qrService.searchQr(resNo);
            try {
                qrService.editQrConfirmRes(QR, resNo);
            }catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            Map<String, Object> data = new HashMap<>();
            data.put("message", "예약 확인에 성공했습니다.");

            return ResponseEntity.ok(data);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"none\"}");
        }
    }
}


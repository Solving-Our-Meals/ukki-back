package com.ohgiraffers.ukki.qr.model.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.ohgiraffers.ukki.qr.model.dao.QrMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import com.ohgiraffers.ukki.common.service.GoogleDriveService;
import java.io.FileInputStream;

@Service
public class QrService {

    private final GoogleDriveService googleDriveService;
    private final QrMapper qrMapper;
    private final String QR_FOLDER_ID = "1Bzigy3LlWfu5wAj7vB5Xdp_QapW76eQG";
    
    @Autowired
    public QrService(QrMapper qrMapper, GoogleDriveService googleDriveService ) {
        this.qrMapper = qrMapper;
        this.googleDriveService = googleDriveService;
    }

    public int qrConfirmation(int resNo, String userId) {
//    qr로 가게 사장 아이디 불러오기
      String storeUserName = qrMapper.resStoreUserName(resNo);
        System.out.println(storeUserName);

        if(storeUserName.equals(userId)) {
            return 1;
        }else {
            return 0;
        }
    }

    public String qrCertificate() throws WriterException {
        try {
            // QR 코드에 예약번호를 포함한 URL 생성
            Integer reservationNo = qrMapper.getLastReservationNo();
            reservationNo = (reservationNo == null || reservationNo == 0) ? 1 : reservationNo + 1;
            String url = "http://localhost:3000/qr/" + reservationNo;  // 실제 운영 URL로 변경 필요
            
            // QR 코드 생성
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, 200, 200);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", baos);
            
            // QR 이미지 파일 생성 및 업로드
            String qrName = "resQr_" + reservationNo;  // 예약번호로 파일명 생성
            MultipartFile qrFile = new MockMultipartFile(
                qrName,
                qrName + ".png",
                "image/png",
                baos.toByteArray()
            );
            
            // Google Drive에 업로드
            String fileId = googleDriveService.uploadFile(qrFile, QR_FOLDER_ID, qrName);
            
            return fileId;  // fileId를 DB의 qr 컬럼에 저장
        } catch (IOException e) {
            throw new RuntimeException("QR 코드 생성 중 오류 발생: " + e.getMessage());
        }
    }


    public void editQrConfirmRes(String qr, int resNo) {
        googleDriveService.deleteFile(qr);
        qrMapper.editQrConfirmRes(resNo);

    }

    public String searchQr(int resNo) {
        return qrMapper.searchQr(resNo);
    }
}

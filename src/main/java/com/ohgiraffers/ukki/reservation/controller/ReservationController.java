package com.ohgiraffers.ukki.reservation.controller;

import com.google.zxing.WriterException;
import com.google.zxing.qrcode.encoder.QRCode;
import com.ohgiraffers.ukki.qr.controller.QrController;
import com.ohgiraffers.ukki.qr.model.service.QrService;
import com.ohgiraffers.ukki.reservation.model.dto.ReservationInfoDTO;
import com.ohgiraffers.ukki.reservation.model.dto.ReservationStoreDTO;
import com.ohgiraffers.ukki.reservation.model.dto.StoreBannerDTO;
import com.ohgiraffers.ukki.reservation.model.service.ReservationService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping(value = "/reservation")
@CrossOrigin(origins = "http://localhost:3000")
public class ReservationController {

    private final ReservationService reservationService;
    private final String SHARED_FOLDER = "\\\\I7E-74\\ukki_nas\\store";
    //    private final String SHARED_FOLDER = "\\\\Desktop-43runa1\\images\\store";
    private final QrService qrService;

    public ReservationController(ReservationService reservationService, QrService qrService){
        this.reservationService = reservationService;
        this.qrService = qrService;
    }

    // 대표 사진
    @GetMapping(value = "/repPhoto")
    public ResponseEntity<String> getRepPhotoName(@RequestParam("storeNo") long storeNo){

        StoreBannerDTO storeBannerDTO = reservationService.getRepPhotoName(storeNo);
        String bannerName = storeBannerDTO.getRepPhoto();
//        System.out.println(storeBannerDTO);

        return ResponseEntity.ok(bannerName);
    }

    @GetMapping(value ="/api/repPhoto")
    public ResponseEntity<Resource> getRepPhoto(@RequestParam("repPhotoName") String repPhotoName){

        try {
            Path file = Paths.get(SHARED_FOLDER).resolve(repPhotoName + ".png");
            //디버깅 확인
//            System.out.println("배너 파일 경로" + file);
            Resource resource = new UrlResource(file.toUri());

            if(resource.exists() && resource.isReadable()){
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; repPhotoName=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // 프로필 사진
    @GetMapping (value = "/profile")
    ResponseEntity<String> getProfileName(@RequestParam("storeNo") long storeNo, ReservationStoreDTO reservationStoreDTO){

//        long storeNo = 5;
        reservationStoreDTO = reservationService.getReservationedStoreInfo(storeNo);
        String profileName = reservationStoreDTO.getStoreProfile();

        return ResponseEntity.ok(profileName);
    }

    @GetMapping(value = "/api/profile")
    public ResponseEntity<Resource> getProfile(@RequestParam("profileName") String profileName){

        try {
            Path file = Paths.get(SHARED_FOLDER).resolve(profileName + ".png");
            Resource resource = new UrlResource(file.toUri());

            if(resource.exists() && resource.isReadable()){
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; profileName=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }

    // 예약 DB에 insert
    @PostMapping(value = "/insert")
    @ResponseBody
    public void insertReservation(@RequestBody ReservationInfoDTO reservationInfoDTO) throws WriterException {

//        System.out.println("예약 DB 드가자");

        // QR코드를 생성하기 위한 필수 파라미터인 이메일 DB에서 받아오기
        String userEmail = reservationService.getEmail(reservationInfoDTO);
//        System.out.println("userEmail = " + userEmail);

        // QR코드 만드는 함수 호출
        QrController qrController = new QrController(qrService);
//        System.out.println("여긴 왔니...?");
        String qrCode = qrController.qrCertificate(reservationInfoDTO.getResDate(), reservationInfoDTO.getResTime(), userEmail);
//        System.out.println("어디까지 왔니1");
        // QR코드 reservationInfoDTO에 넣기
        reservationInfoDTO.setQr(qrCode);
//        System.out.println("어디까지 왔니2");
//        System.out.println("reservationInfo : " + reservationInfoDTO);
//        System.out.println("어디까지 왔니3");

        reservationService.insertReservation(reservationInfoDTO);

        reservationService.increaseReservation(reservationInfoDTO.getUserNo());
    }
}

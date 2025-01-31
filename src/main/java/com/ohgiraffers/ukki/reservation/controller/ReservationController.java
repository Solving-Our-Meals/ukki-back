package com.ohgiraffers.ukki.reservation.controller;

import com.google.zxing.WriterException;
import com.google.zxing.qrcode.encoder.QRCode;
import com.ohgiraffers.ukki.qr.controller.QrController;
import com.ohgiraffers.ukki.qr.model.service.QrService;
import com.ohgiraffers.ukki.reservation.model.dto.ReservationInfoDTO;
import com.ohgiraffers.ukki.reservation.model.dto.ReservationStoreDTO;
import com.ohgiraffers.ukki.reservation.model.dto.StoreBannerDTO;
import com.ohgiraffers.ukki.reservation.model.service.ReservationService;
import org.apache.coyote.Response;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping(value = "/reservation")
@CrossOrigin(origins = "http://localhost:3000")
public class ReservationController {

    private final ReservationService reservationService;
    private final QrService qrService;

    public ReservationController(ReservationService reservationService, QrService qrService){
        this.reservationService = reservationService;
        this.qrService = qrService;
    }

    // 대표 사진
    @GetMapping(value = "/repPhoto")
    public ResponseEntity<?> getRepPhotoName(@RequestParam("storeNo") long storeNo){
        try{

            StoreBannerDTO storeBannerDTO = reservationService.getRepPhotoName(storeNo);
            String bannerName = storeBannerDTO.getRepPhoto();
    //        System.out.println(storeBannerDTO);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(bannerName);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("예약한 가게 사진을 불러오는 도중에 에러가 발생했습니다.");
        }
    }

    @GetMapping(value ="/api/repPhoto")
    public ResponseEntity<byte[]> getRepPhoto(@RequestParam("repPhotoName") String repPhotoName){
        try {
            if (repPhotoName == null || repPhotoName.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("유효하지 않은 가게 배너 파일명입니다.".getBytes());
            }

            String imageUrlWithId = "https://drive.google.com/uc?id=" + repPhotoName;

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<byte[]> response = restTemplate.exchange(imageUrlWithId, HttpMethod.GET, null, byte[].class);

            HttpHeaders headers = new HttpHeaders();
            String contentType = response.getHeaders().getContentType() != null ?
                    response.getHeaders().getContentType().toString() :
                    "application/octet-stream";

            headers.set("Content-Type", contentType);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(response.getBody());

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("배너 이미지를 불러오는 도중에 오류가 발생했습니다.".getBytes());
        }
    }

    // 프로필 사진
    @GetMapping (value = "/profile")
    ResponseEntity<?> getProfileName(@RequestParam("storeNo") long storeNo, ReservationStoreDTO reservationStoreDTO){
        try{

//          long storeNo = 5;
            reservationStoreDTO = reservationService.getReservationedStoreInfo(storeNo);
            String profileName = reservationStoreDTO.getStoreProfile();

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(profileName);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("예약한 가게 프로필 사진 불러오는 도중에 에러가 발생했습니다.");
        }
    }

    @GetMapping(value = "/api/profile")
    public ResponseEntity<byte[]> getProfile(@RequestParam("profileName") String profileName){
        try {
            if (profileName == null || profileName.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("유효하지 않은 가게 배너 파일명입니다.".getBytes());
            }

            String imageUrlWithId = "https://drive.google.com/uc?id=" + profileName;

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<byte[]> response = restTemplate.exchange(imageUrlWithId, HttpMethod.GET, null, byte[].class);

            HttpHeaders headers = new HttpHeaders();
            String contentType = response.getHeaders().getContentType() != null ?
                    response.getHeaders().getContentType().toString() :
                    "application/octet-stream";

            headers.set("Content-Type", contentType);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(response.getBody());

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("배너 이미지를 불러오는 도중에 오류가 발생했습니다.".getBytes());
        }
    }

    // 예약 DB에 insert
    @PostMapping(value = "/insert")
    @ResponseBody
    public ResponseEntity<?> insertReservation(@RequestBody ReservationInfoDTO reservationInfoDTO) throws WriterException {
        try{

            String resultMessage = "";
            // 중복 예약 정보가 있는지 확인
            System.out.println("reservation 정보 : " + reservationInfoDTO);

            ReservationInfoDTO isExistReservation = reservationService.checkExistReservation(reservationInfoDTO);

            System.out.println("isExistReservation = " + isExistReservation);

            if(isExistReservation == null){
                System.out.println("예약 DB 드가자");

                // QR코드를 생성하기 위한 필수 파라미터인 이메일 DB에서 받아오기
                String userEmail = reservationService.getEmail(reservationInfoDTO);
    //        System.out.println("userEmail = " + userEmail);

                // QR코드 만드는 함수 호출
    //        QrController qrController = new QrController(qrService);
    //        String qrCode = qrController.qrCertificate(reservationInfoDTO.getResDate(), reservationInfoDTO.getResTime(), userEmail);
                String qrCode = qrService.qrCertificate();
    //         QR코드 reservationInfoDTO에 넣기
                reservationInfoDTO.setQr(qrCode);
                System.out.println("reservationInfo : " + reservationInfoDTO);

                reservationService.insertReservation(reservationInfoDTO);

                reservationService.increaseReservation(reservationInfoDTO.getUserNo());

                resultMessage = "예약 성공";
            } else {
                resultMessage = "예약 실패";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(resultMessage);
        } catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("예약 정보를 저장하는 도중에 에러가 발생했습니다.");
        }
    }
}

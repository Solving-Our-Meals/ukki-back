package com.ohgiraffers.ukki.reservation.controller;

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
public class ReservationController {

    private final ReservationService reservationService;
//    private final String SHARED_FOLDER = "\\\\I7E-74\\ukki_nas\\store";
    private final String SHARED_FOLDER = "\\\\Desktop-43runa1\\images";

    public ReservationController(ReservationService reservationService){
        this.reservationService = reservationService;
    }

    // 대표 사진
    @GetMapping(value = "/{storeNo}/repPhoto")
    public ResponseEntity<String> getRepPhotoName(@PathVariable("storeNo") long storeNo){

        StoreBannerDTO storeBannerDTO = reservationService.getRepPhotoName(storeNo);
        String bannerName = storeBannerDTO.getRepPhoto();
        System.out.println(storeBannerDTO);

        return ResponseEntity.ok(bannerName);
    }

    @GetMapping(value ="/api/repPhoto")
    public ResponseEntity<Resource> getRepPhoto(@RequestParam("repPhotoName") String repPhotoName){

        try {
            Path file = Paths.get(SHARED_FOLDER).resolve(repPhotoName + ".png");
            //디버깅 확인
            System.out.println("배너 파일 경로" + file);
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
    @GetMapping (value = "/{storeNo}/profile")
    ResponseEntity<String> getProfileName(@PathVariable("storeNo") long storeNo, ReservationStoreDTO reservationStoreDTO){

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
}

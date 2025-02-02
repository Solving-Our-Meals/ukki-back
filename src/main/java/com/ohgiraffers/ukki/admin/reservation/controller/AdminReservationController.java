package com.ohgiraffers.ukki.admin.reservation.controller;

import com.ohgiraffers.ukki.admin.reservation.model.dto.MonthlyNoShowDTO;
import com.ohgiraffers.ukki.admin.reservation.model.dto.AdminReservationListDTO;
import com.ohgiraffers.ukki.admin.reservation.model.dto.AdminReservationInfoDTO;
import com.ohgiraffers.ukki.admin.reservation.model.dto.ThisWeekReservationDTO;
import com.ohgiraffers.ukki.admin.reservation.model.service.AdminReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ohgiraffers.ukki.common.service.GoogleDriveService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;

@RestController
@RequestMapping("/admin/reservations")
public class AdminReservationController {

    private final AdminReservationService adminReservationService;
    private final GoogleDriveService googleDriveService;

    @Autowired
    public AdminReservationController(AdminReservationService adminReservationService, GoogleDriveService googleDriveService){
        this.adminReservationService = adminReservationService;
        this.googleDriveService = googleDriveService;
    }

    @GetMapping("/weekly")
    public ResponseEntity<?> weeklyReservation() {
        try {
            ThisWeekReservationDTO thisWeek = adminReservationService.weeklyReservation();
            if(thisWeek==null){
                thisWeek = new ThisWeekReservationDTO();
                thisWeek.setMon(0);
                thisWeek.setTue(0);
                thisWeek.setWed(0);
                thisWeek.setThu(0);
                thisWeek.setFri(0);
                thisWeek.setSat(0);
                thisWeek.setSun(0);
            }
            System.out.println(thisWeek);
            return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(thisWeek);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body("주간 예약 정보를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @GetMapping("/today")
    public ResponseEntity<?> todayReservation() {
        try {
            int today = adminReservationService.todayReservation();
            System.out.println(today);
            Map<String, Integer> response = new HashMap<>();
            response.put("todayReservation", today);
            return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(response);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body("오늘 예약 정보를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @GetMapping("/noshow")
    public ResponseEntity<?> monthlyNoShowReservation() {
        try {
            MonthlyNoShowDTO noShow = adminReservationService.monthlyNoShowReservation();


            System.out.println(noShow);

            return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(noShow);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body("이번 달 노쇼 정보를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> listReservations(@RequestParam(required = false) String category, @RequestParam(required = false) String word) {
        try {
            System.out.println(category);
            System.out.println(word);

            List<AdminReservationListDTO> resTodayList = adminReservationService.searchRes(category, word);
            List<AdminReservationListDTO> resEndList = adminReservationService.searchEndRes(category, word);

            List<AdminReservationListDTO> resList = new ArrayList<>(resTodayList);

            resList.addAll(resEndList);

            System.out.println(resList);

            return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(resList);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body("예약리스트를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @GetMapping("/total")
    public ResponseEntity<?> totalReservations() {
        int totalTodayRes = adminReservationService.totalTodayReservation();
        int totalEndRes = adminReservationService.totalEndReservation();

        int totalRes = totalTodayRes + totalEndRes;


        Map<String, Integer> response = new HashMap<>();
        response.put("totalReservation", totalRes);

        return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(response);
    }

    @GetMapping("/info/today/{resNo}")
    public ResponseEntity<?> todayResInfo(@PathVariable int resNo) {
        try {

            System.out.println(resNo);

            AdminReservationInfoDTO resInfo = adminReservationService.todayResInfo(resNo);

            return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(resInfo);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body("예약상세 정보를 불러오는 도중 에러가 발생했습니다.");
        }

    }

    @GetMapping("/info/end/{resNo}")
    public ResponseEntity<?> endResInfo(@PathVariable int resNo) {
        try {

            AdminReservationInfoDTO resInfo = adminReservationService.endResInfo(resNo);

            return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(resInfo);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body("예약상세 정보를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @DeleteMapping("/info/today/{resNo}")
    public ResponseEntity<?> deleteTodayRes(@PathVariable int resNo) {
        try{
            AdminReservationInfoDTO resInfo = adminReservationService.todayResInfo(resNo);
            if(resInfo.getQr() != "expired"){
                googleDriveService.deleteFile(resInfo.getQr());
            }

            adminReservationService.deleteTodayRes(resNo);
            Map<String, String> response = new HashMap<>();
            response.put("message", "삭제 성공");

            return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(response);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body("예약정보 삭제 도중 에러가 발생했습니다.");
        }
    }

    @DeleteMapping("/info/end/{resNo}")
    public ResponseEntity<?> deleteEndRes(@PathVariable int resNo) {
        try{
            System.out.println("왔다 " + resNo);
            AdminReservationInfoDTO resInfo = adminReservationService.endResInfo(resNo);
            System.out.println(resInfo);
            
            if (resInfo == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("해당 예약 정보를 찾을 수 없습니다.");
            }

            String qr = resInfo.getQr();
            if(qr != null && !"expired".equals(qr)){
                googleDriveService.deleteFile(resInfo.getQr());
            }

            adminReservationService.deleteEndRes(resNo);
            Map<String, String> response = new HashMap<>();
            response.put("message", "삭제 성공");

            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body("예약정보 삭제 도중 에러가 발생했습니다.");
        }
    }
}

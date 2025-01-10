package com.ohgiraffers.ukki.admin.reservation.controller;

import com.ohgiraffers.ukki.admin.reservation.model.dto.MonthlyNoShowDTO;
import com.ohgiraffers.ukki.admin.reservation.model.dto.ThisWeekReservationDTO;
import com.ohgiraffers.ukki.admin.reservation.model.service.AdminReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/reservations")
public class AdminReservationController {

    private final AdminReservationService adminReservationService;

    @Autowired
    public AdminReservationController(AdminReservationService adminReservationService){
        this.adminReservationService = adminReservationService;
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
            return ResponseEntity.ok(thisWeek);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
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
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("오늘 예약 정보를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @GetMapping("/noshow")
    public ResponseEntity<?> monthlyNoShowReservation() {
        try {
            MonthlyNoShowDTO noShow = adminReservationService.monthlyNoShowReservation();


            System.out.println(noShow);

            return ResponseEntity.ok(noShow);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("이번 달 노쇼 정보를 불러오는 도중 에러가 발생했습니다.");
        }
    }

}

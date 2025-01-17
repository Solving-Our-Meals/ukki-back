package com.ohgiraffers.ukki.store.controller;

import com.ohgiraffers.ukki.store.model.dto.*;
import com.ohgiraffers.ukki.store.model.service.BossService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/boss/mypage")
public class BossController {

    @Autowired
    private BossService bossService;

    // 예약 현황 조회
    @GetMapping("/reservation-status")
    public ResponseEntity<List<ReservationDTO>> getReservationStatus(@RequestParam int storeNo) {
        try {
            // 로그 추가
            System.out.println("storeNo: " + storeNo);
            List<ReservationDTO> reservations = bossService.getReservationStatus(storeNo);
            if (reservations.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    // 예약 인원 리스트 조회
    @GetMapping("/reservation-people-list")
    public ResponseEntity<List<ReservationDTO>> getReservationPeopleList(@RequestParam int storeNo) {
        try {
            List<ReservationDTO> reservations = bossService.getReservationPeopleList(storeNo);
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 예약 가능 인원 조회
    @GetMapping("/available-slots")
    public ResponseEntity<AvailableSlotsDTO> getAvailableSlots(@RequestParam int storeNo, @RequestParam String reservationDate) {
        try {
            AvailableSlotsDTO availableSlots = bossService.getAvailableSlots(storeNo, reservationDate);
            return ResponseEntity.ok(availableSlots);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 이번 주 예약 수 조회
    @GetMapping("/weekly-reservation-count")
    public ResponseEntity<WeeklyReservationCountDTO> getWeeklyReservationCount(@RequestParam int storeNo) {
        try {
            WeeklyReservationCountDTO count = bossService.getWeeklyReservationCount(storeNo);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 오늘 예약 수 조회
    @GetMapping("/today-reservation-count")
    public ResponseEntity<Integer> getTodayReservationCount(@RequestParam int storeNo) {
        try {
            int count = bossService.getTodayReservationCount(storeNo);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 다음 예약 가능한 시간 조회
    @GetMapping("/next-available-time")
    public ResponseEntity<String> getNextAvailableTime(@RequestParam int storeNo, @RequestParam String resDate, @RequestParam String currentTime) {
        try {
            String nextTime = bossService.getNextAvailableTime(storeNo, resDate, currentTime);
            return ResponseEntity.ok(nextTime);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 요일별, 시간별 예약 가능 인원 조회
    @PostMapping("/res-pos-num")
    public ResponseEntity<List<DayResPosNumDTO>> getResPosNum(@RequestBody StoreResPosNumDTO storeResPosNumDTO) {
        try {
            List<DayResPosNumDTO> result = bossService.getResPosNum(storeResPosNumDTO);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

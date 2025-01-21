package com.ohgiraffers.ukki.store.controller;

import com.ohgiraffers.ukki.reservation.model.service.ReservationService;
import com.ohgiraffers.ukki.store.model.dto.*;
import com.ohgiraffers.ukki.store.model.service.BossService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
@RestController
@RequestMapping("/boss/mypage")
public class BossController {

    @Autowired
    private BossService bossService;
    @Autowired
    private ReservationService reservationService;

    // 가게 정보 조회
    @GetMapping("/getStoreInfo")
    public ResponseEntity<StoreInfoDTO> getStoreInfo(@ModelAttribute StoreInfoDTO storeInfoDTO, @RequestParam("userNo") long userNo){
        storeInfoDTO = bossService.getStoreInfo(userNo);
        return ResponseEntity.ok(storeInfoDTO);
    }

    // 예약 현황 조회
    @GetMapping("/reservation-status")
    public ResponseEntity<List<ReservationDTO>> getReservationStatus(@RequestParam int storeNo) {
        try {
            List<ReservationDTO> reservations = bossService.getReservationStatus(storeNo);
            if (reservations.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
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

    // 예약 가능 인원 조회
    @PostMapping("/getResPosNum")
    public ResponseEntity<List<DayResPosNumDTO>> getResPosNum(@RequestBody StoreResPosNumDTO storeResPosNumDTO) {
        try {
            List<DayResPosNumDTO> result = bossService.getResPosNum(storeResPosNumDTO);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 예약 가능 인원 업데이트
    @PostMapping("/updateAvailableSlots")
    public ResponseEntity<String> updateAvailableSlots(
            @RequestParam int storeNo,
            @RequestParam String reservationDate,
            @RequestParam String reservationTime,
            @RequestParam int resPosNumber) {

        // 예약 날짜를 LocalDate로 변환
        LocalDate date = LocalDate.parse(reservationDate); // String을 LocalDate로 변환

        // 유효성 검사: 예약 가능한 인원 수는 음수일 수 없습니다.
        if (resPosNumber < 0) {
            return ResponseEntity.badRequest().body("Slot number cannot be negative");
        }

        try {
            // 변환된 LocalDate와 다른 파라미터를 서비스로 전달
            bossService.updateAvailableSlots(storeNo, date, reservationTime, resPosNumber);
            return ResponseEntity.ok("Available slots updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update available slots: " + e.getMessage());
        }
    }




    // 예약 리스트 조회
    @GetMapping("/reservations-list")
    public ResponseEntity<List<ReservationDTO>> getReservationListForTime(
            @RequestParam int storeNo,
            @RequestParam String reservationDate,
            @RequestParam(required = false) String reservationTime) {

        try {
            List<ReservationDTO> reservations = bossService.getReservationListForTime(storeNo, reservationDate, reservationTime);
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getAvailableSlots")
    public ResponseEntity<Map<String, Object>> getAvailableSlots(@RequestParam long storeNo) {
        try {
            int availableSlots = bossService.getAvailableSlots(storeNo);
            Map<String, Object> response = new HashMap<>();
            response.put("availableSlots", availableSlots);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch available slots"));
        }
    }

    // 최신 리뷰 받아오기
    @GetMapping("/recentReview")
    public ReviewContentDTO getRecentReview(@ModelAttribute ReviewContentDTO reviewContentDTO, @RequestParam("storeNo") long storeNo){
        return bossService.getRecentReview(storeNo);
    }

    // 리뷰 리스트 가져오기
    @GetMapping("/reviewList")
    public List<ReviewDTO> getReviewList(@RequestParam("storeNo") long storeNo) {
        return (List<ReviewDTO>) bossService.getReviewList(storeNo);
    }

    // 리뷰 신고
    @PostMapping("/reviewReport")
    public ResponseEntity<String> reportReview(@RequestParam("storeNo") long storeNo, @RequestBody ReportReviewDTO reportReviewDTO){
        try {
            bossService.reportReview(reportReviewDTO);
            bossService.updateReportCount(reportReviewDTO.getReviewNo());
            return ResponseEntity.ok("Review reported successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to report review");
        }
    }

    // 최근 문의 내역 조회
    @GetMapping("/recentInquiry")
    public InquiryDTO getRecentInquiry(@RequestParam("storeNo") long storeNo, @RequestParam("userNo") long userNo){
        List<InquiryDTO> recentInquiries = bossService.getRecentInquiryList(userNo);
        List<InquiryDTO> recentReports = bossService.getRecentReportList(storeNo);
        recentInquiries.addAll(recentReports);
        recentInquiries.sort(Comparator.comparing(InquiryDTO::getInquiryDate).reversed());
        return recentInquiries.isEmpty() ? null : recentInquiries.get(0);
    }
}

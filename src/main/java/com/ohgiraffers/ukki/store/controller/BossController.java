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

    // 가게 정보 조회
    @GetMapping("/getStoreInfo")
    public ResponseEntity<StoreInfoDTO> getStoreInfo(@ModelAttribute StoreInfoDTO storeInfoDTO, @RequestParam("userNo") long userNo){

        storeInfoDTO = bossService.getStoreInfo(userNo);
        System.out.println("사장님 가게" + storeInfoDTO);

        return ResponseEntity.ok(storeInfoDTO);
    }

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

    // 최신 리뷰 받아오기
    @GetMapping("/recentReview")
    public ReviewContentDTO getRecentReview(@ModelAttribute ReviewContentDTO reviewContentDTO, @RequestParam("storeNo") long storeNo){

        reviewContentDTO = bossService.getRecentReview(storeNo);

        return reviewContentDTO;
    }

    // 리뷰 리스트 가져오기
    @GetMapping("/reviewList")
    public ReviewDTO getReviewList(@ModelAttribute ReviewDTO reviewDTO, @RequestParam("storeNo") long storeNo) {

        reviewDTO = bossService.getReviewList(storeNo);

        System.out.println("리뷰 리스트 가져옴" + reviewDTO);

        return reviewDTO;
    }

    // 리뷰 정보 가져오기(상세조회)
    @GetMapping("/getReviewInfo")
    public DetailReviewInfoDTO getReviewInfo(@RequestParam("reviewNo") Long reviewNo) {
//        if (reviewNo == null || reviewNo <= 0) {
//            throw new IllegalArgumentException("유효한 reviewNo가 필요합니다.");
//        }

        DetailReviewInfoDTO detailReviewInfoDTO = bossService.getReviewInfo(reviewNo);

        System.out.println("reviewContentDTO = " + detailReviewInfoDTO);

        return detailReviewInfoDTO;
    }

    // 리뷰 신고
    @PostMapping("/reviewReport")
    @ResponseBody
    public void reportReview(@RequestParam("storeNo") long storeNo, @RequestBody ReportReviewDTO reportReviewDTO){

        System.out.println("..dhdhdhdhd.." + reportReviewDTO);

        bossService.reportReview(reportReviewDTO);
    }


}
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
    private ReservationService reservationService;  // ReservationService 주입


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
// 예시: 예약 가능 시간 조회 API
//    @GetMapping("/available-slots")
//    public ResponseEntity<List<String>> getAvailableSlots(
//            @RequestParam("storeNo") long storeNo,
//            @RequestParam("reservationDate") String reservationDate) {
//
//        try {
//            List<String> availableSlots = reservationService.getAvailableSlots(storeNo, reservationDate);
//
//            // 데이터가 없을 경우 빈 배열 반환
//            if (availableSlots == null) {
//                availableSlots = new ArrayList<>();
//            }
//
//            return ResponseEntity.ok(availableSlots);
//        } catch (Exception e) {
//            // 예외 발생 시 500 상태 코드와 함께 에러 메시지 반환
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonList("Error fetching available slots"));
//        }
//    }

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

    // 예약 가능 인원 업데이트
    @PostMapping("/updateAvailableSlots")
    public ResponseEntity<String> updateAvailableSlots(@RequestParam int storeNo, @RequestParam int newSlots) {
        try {
            if (newSlots < 0) {
                return ResponseEntity.badRequest().body("Slot 수는 0보다 작을 수 없습니다.");
            }

            // 매장 예약 가능 인원 업데이트
            bossService.updateAvailableSlots(storeNo, newSlots);

            return ResponseEntity.ok("Available slots updated successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update available slots: " + e.getMessage());
        }
    }


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



                                                             
    // 예약 가능 인원 업데이트
//    @PostMapping("/updateAvailableSlots")
//    public ResponseEntity<String> updateAvailableSlots(@RequestParam long storeNo, @RequestParam int newSlots) {
//        try {
//            if (newSlots < 0) {
//                return ResponseEntity.badRequest().body("Slot 수는 0보다 작을 수 없습니다.");
//            }
//
//            bossService.updateAvailableSlots(storeNo, newSlots);
//            return ResponseEntity.ok("Available slots updated successfully");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update available slots: " + e.getMessage());
//        }
//    }


    @GetMapping("/mypage/getAvailableSlots")
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


    @GetMapping("/getWeeklyReservations")
    public ResponseEntity<List<ReservationInfoDTO>> getWeeklyReservations(@RequestParam long storeNo) {
        LocalDate today = LocalDate.now();
        LocalDate sevenDaysLater = today.plusDays(7);

        List<ReservationInfoDTO> reservations = bossService.getReservationsForPeriod(storeNo, today, sevenDaysLater);
        return ResponseEntity.ok(reservations);
    }
    @GetMapping("/store/{storeNo}/reservation")
    public ResponseEntity<Integer> getAvailableReservationPosNum(
            @PathVariable int storeNo,
            @RequestParam String reservationDate,
            @RequestParam String reservationTime) {

        Integer availablePosNum = reservationService.getAvailablePosNum(storeNo, reservationDate, reservationTime);

        return ResponseEntity.ok(availablePosNum);
    }
    @PutMapping("/store/{storeNo}/reservation")
    public ResponseEntity<String> updateReservationPosNum(
            @PathVariable int storeNo,
            @RequestParam String reservationDate,
            @RequestParam String reservationTime,
            @RequestParam int newPosNumber) {

        reservationService.updateReservationPosNum(storeNo, reservationDate, reservationTime, newPosNumber);

        return ResponseEntity.ok("Reservation pos number updated successfully");
    }


    @PostMapping("/store/{storeNo}/reservation")
    public ResponseEntity<String> insertReservationPosNum(
            @PathVariable int storeNo,
            @RequestParam String reservationDate,
            @RequestParam String reservationTime,
            @RequestParam int newPosNumber) {

        reservationService.updateReservationPosNum(storeNo, reservationDate, reservationTime, newPosNumber);

        return ResponseEntity.ok("Reservation pos number updated successfully");

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

        // 리뷰 신고 등록
        bossService.reportReview(reportReviewDTO);

        // 리뷰 신고 기록 +1
        bossService.updateReportCount(reportReviewDTO.getReviewNo());
    }
    // 문의 내역 조회
    @GetMapping(value = "/inquiryList")
    public List<InquiryDTO> getInquiryList(@RequestParam("storeNo") long storeNo, @RequestParam("userNo") long userNo, @RequestParam(value = "searchWord" , required = false) String searchWord){

        // INQUIRY 테이블에서 가져오기
        List<InquiryDTO> inquiryList =  bossService.getInquiryList(userNo, searchWord);

        // REVIEW_REPORT 테이블에서 가져오기
        List<InquiryDTO> reportList = bossService.getReportList(storeNo, searchWord);

        inquiryList.addAll(reportList);
        inquiryList.sort(Comparator.comparing(InquiryDTO::getInquiryDate).reversed());

        System.out.println("inquiryList111111 = " + inquiryList);

        return inquiryList;
    }

    // 최근 문의 내역 조회
    @GetMapping(value = "/recentInquiry")
    public InquiryDTO getRecentInquiryList(@RequestParam("storeNo") long storeNo, @RequestParam("userNo") long userNo){
        List<InquiryDTO> RecentInquirytList =  bossService.getRecentInquiryList(userNo);

        // REVIEW_REPORT 테이블에서 가져오기
        List<InquiryDTO> RecentReportList = bossService.getRecentReportList(storeNo);

        RecentInquirytList.addAll(RecentReportList);
        RecentInquirytList.sort(Comparator.comparing(InquiryDTO::getInquiryDate).reversed());

        // 가장 마지막 요소 가져오기
        InquiryDTO lastInquiry = RecentInquirytList.get(0);

        return lastInquiry;
    }



}


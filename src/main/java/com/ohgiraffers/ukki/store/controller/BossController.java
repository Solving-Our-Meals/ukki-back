package com.ohgiraffers.ukki.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohgiraffers.ukki.reservation.model.service.ReservationService;
import com.ohgiraffers.ukki.store.model.dto.*;
import com.ohgiraffers.ukki.store.model.service.BossService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.Resource;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/boss/mypage")
public class BossController {

    @Autowired
    private BossService bossService;
    @Autowired

    private ReservationService reservationService;


    private final String INQUIRY_SHARE_DRIVE = "\\\\I7E-74\\ukki_nas\\inquiry";


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

        return inquiryList;

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

    @GetMapping(value = "getSpecificInquiry")
    public InquiryDTO getSpecificInquiry(@RequestParam("inquiryNo") long inquiryNo, @RequestParam("categoryNo") long categoryNo){

        InquiryDTO inquiryDTO;
        // 리뷰 신고일 경우
        if(categoryNo == 0){
            String table = "TBL_REVIEW_REPORT";
            inquiryDTO = bossService.getInquiryInfo(inquiryNo, table);
        } else {
            String table = "TBL_INQUIRY";
            inquiryDTO = bossService.getInquiryInfo(inquiryNo, table);
        }

        return inquiryDTO;
    }

    @DeleteMapping("/deleteInquiry")
    public void deleteInquiry(@RequestParam("inquiryNo") long inquiryNo, @RequestParam("categoryNo") long categoryNo, @RequestParam("reviewNo") long reviewNo){

        if(categoryNo == 0){
            // tbl_review_report에서 삭제
            bossService.deleteReviewReport(inquiryNo);
            // review에서 reportCount -1
            bossService.decreaseReportCount(reviewNo);
        } else {
            String existingFile = bossService.getFileName(inquiryNo);

            bossService.deleteInquiry(inquiryNo);

            // 여기 파일 삭제
            if(existingFile != null && !existingFile.isEmpty()){
                File fileToDelete = new File(INQUIRY_SHARE_DRIVE + "/" + existingFile);
                if(fileToDelete.exists()){
                    boolean deleted = fileToDelete.delete();
                    if(!deleted){
                        throw new RuntimeException("파일 삭제 실패");
                    }
                }
            }

        }
    }

    @PutMapping(value = "/updateInquiry", consumes = "multipart/form-data")
    @ResponseBody
    public void updateInquiry(@RequestParam("inquiryNo") long inquiryNo,
                              @RequestParam("categoryNo") long categoryNo,
                              @RequestParam("params") String params,
                              @RequestParam("reviewNo") long reviewNo,
                              @RequestPart(value = "inquiryFile", required = false) MultipartFile singleFile){

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> paramMap;
        InquiryDTO inquiryDTO = new InquiryDTO();

        System.out.println("문의 수정입니다. = " + params);

        try {
            paramMap = objectMapper.readValue(params, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        paramMap.forEach((key, value) -> {
            switch (key) {
                case "inquiryTitle":
                    inquiryDTO.setInquiryTitle(value);
                    break;
                case "inquiryContent":
                    inquiryDTO.setInquiryContent(value);
                    break;
                case "inquiryDate":
                    inquiryDTO.setInquiryDate(value);
                    break;
            }
        });



        // 1. 기존 파일 삭제 로직 (파일이 존재하고 새 파일을 첨부하는 경우)
        if (categoryNo != 0) {
            String existingFile = bossService.getFileName(inquiryNo);// DB에서 기존 파일 이름을 가져옵니다.
            System.out.println("existingFile = " + existingFile);
            // 2. 클라이언트에서 새로운 파일을 첨부한 경우, 기존 파일 삭제 후 새로운 파일 저장
            if (singleFile != null && !singleFile.isEmpty()) {
                if (existingFile != null && !existingFile.isEmpty()) {
                    // 기존 파일이 존재하면 드라이브에서 삭제
                    File existingFileOnDisk = new File(INQUIRY_SHARE_DRIVE + "/" + existingFile);
                    if (existingFileOnDisk.exists()) {
                        boolean deleted = existingFileOnDisk.delete(); // 기존 파일 삭제
                        if (!deleted) {
                            System.out.println("기존 파일 삭제 실패: " + existingFileOnDisk.getPath());
                            throw new RuntimeException("기존 파일 삭제 실패: " + existingFileOnDisk.getPath());
                        } else {
                            System.out.println("기존 파일 삭제 성공: " + existingFileOnDisk.getPath());
                        }
                    }
                }

                // 새로운 파일 업로드
                String filePath = INQUIRY_SHARE_DRIVE;
                File dir = new File(filePath);
                if (!dir.exists()) {
                    dir.mkdirs();  // 디렉토리가 없으면 생성
                }

                String originFileName = singleFile.getOriginalFilename();
                String fullPath = filePath + "/" + originFileName;

                try {
                    singleFile.transferTo(new File(fullPath)); // 새로운 파일을 저장
                    inquiryDTO.setFile(originFileName); // DB에 새로운 파일 이름 설정
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException("파일 저장 오류", e);
                }

            } else {
                // 클라이언트가 파일을 첨부하지 않으면 기존 파일을 삭제하고, DB에서 파일 정보도 null로 설정
                if (existingFile != null && !existingFile.isEmpty()) {
                    File existingFileOnDisk = new File(INQUIRY_SHARE_DRIVE + "/" + existingFile);
                    if (existingFileOnDisk.exists()) {
                        boolean deleted = existingFileOnDisk.delete();  // 기존 파일 삭제
                        if (!deleted) {
                            System.out.println("기존 파일 삭제 실패: " + existingFileOnDisk.getPath());
                            throw new RuntimeException("기존 파일 삭제 실패: " + existingFileOnDisk.getPath());
                        } else {
                            System.out.println("기존 파일 삭제 성공: " + existingFileOnDisk.getPath());
                        }
                    }
                }
                inquiryDTO.setFile(null);  // DB에서 파일 정보 삭제 (null로 설정)
            }
        }

        // DB 업데이트
        if (categoryNo == 0) {
            System.out.println("inquiryDTO 입니다. :" + inquiryDTO);
            bossService.updateReviewReport(inquiryDTO, inquiryNo);
        } else {
            System.out.println("inquiryDTO 입니다. :" + inquiryDTO);
            bossService.updateInquiry(inquiryDTO, inquiryNo);
        }

    }

    @GetMapping("/downloadFile")
    public ResponseEntity<Resource> downloadFile(@RequestParam("fileName") String fileName) {
        System.out.println("Download request received for file: " + fileName);

        // 파일 경로 생성
        Path filePath = Paths.get(INQUIRY_SHARE_DRIVE, fileName);
        Resource resource;

        System.out.println("filePath = " + filePath);

        try {
            resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                // 파일 이름 URL 인코딩
                String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString()).replace('+', ' ');

                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();  // 파일이 없으면 404 반환
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  // URL 처리 오류시 500
        } catch (UnsupportedEncodingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  // 인코딩 오류시 500
        }
    }

}

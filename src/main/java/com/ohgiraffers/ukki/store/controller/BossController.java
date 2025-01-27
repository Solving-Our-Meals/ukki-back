package com.ohgiraffers.ukki.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohgiraffers.ukki.admin.reservation.model.dto.ThisWeekReservationDTO;
import com.ohgiraffers.ukki.store.model.dao.BossMapper;
import com.ohgiraffers.ukki.store.model.dto.*;
import com.ohgiraffers.ukki.store.model.service.BossService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.format.annotation.DateTimeFormat;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/boss/mypage")
public class BossController {

    @Autowired
    private BossService bossService;
    @Autowired
    private BossMapper bossMapper;

    private final String INQUIRY_SHARE_DRIVE = "\\\\I7E-74\\ukki_nas\\inquiry";

    // 가게 정보 조회
    @GetMapping("/getStoreInfo")
    public ResponseEntity<StoreInfoDTO> getStoreInfo(@RequestParam("userNo") long userNo) {
        // 사용자 번호로 가게 정보 가져오기
        StoreInfoDTO storeInfoDTO = bossService.getStoreInfo(userNo);

        // 가게 정보가 없으면 404 반환 (없을 경우에 대한 처리)
        if (storeInfoDTO == null) {
            return ResponseEntity.notFound().build();
        }

        // 가게 정보가 있으면 200 OK와 함께 반환
        return ResponseEntity.ok(storeInfoDTO);
    }


    // 예약 현황 조회
// 예약 현황 조회
    @GetMapping("/reservation-status")
    public ResponseEntity<List<StoreResPosNumDTO>> getReservationStatus(
            @RequestParam Long storeNo,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate reservationDate,
            @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime reservationTime) {

        // DB에서 예약 가능한 인원 수 조회
        List<StoreResPosNumDTO> reservations = bossService.getReservationStatus(storeNo, reservationDate, reservationTime);
        System.out.println("reservations: " + reservations);  // 로그 추가

        // 예약 정보가 없으면 빈 리스트 반환
        if (reservations == null || reservations.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        // 최신 예약 정보 가져오기
        StoreResPosNumDTO latestReservation = reservations.get(0);  // 내림차순 정렬로 최신값이 첫 번째
        return ResponseEntity.ok(Collections.singletonList(latestReservation));
    }



    @GetMapping("/reservations-list")
    public ResponseEntity<List<ReservationDTO>> getReservationStatus(
            @RequestParam long storeNo,
            @RequestParam LocalDate reservationDate,  // String으로 받음
            @RequestParam(required = false) LocalTime reservationTime) {

        try {

            List<ReservationDTO> reservations = bossService.getReservationList(storeNo, reservationDate, reservationTime);
            return ResponseEntity.ok(reservations);
        } catch (DateTimeParseException e) {
            // 날짜나 시간이 잘못된 형식일 경우
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            // 일반적인 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }





    @GetMapping("/weekly-reservation-count")
    public ResponseEntity<?> getWeeklyReservationCount(@RequestParam long storeNo) {
        try {
            ThisWeekReservationDTO thisWeek = bossService.getWeeklyReservationCount(storeNo);
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


    // 오늘 예약 수 조회
    @GetMapping("/today-reservation-count")
    public ResponseEntity<Integer> getTodayReservationCount(@RequestParam long storeNo) {
        try {
            int count = bossService.getTodayReservationCount(storeNo);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0);  // 기본 값 0을 별도로 분리
        }
    }



    // 예약 가능 인원 삽입
    @PostMapping("/insertAvailableSlots")
    public ResponseEntity<String> insertAvailableSlots(@RequestBody StoreResPosNumDTO storeResPosNumDTO) {
        Long storeNo = storeResPosNumDTO.getStoreNo();
        LocalDate reservationDate = storeResPosNumDTO.getReservationDate();
        LocalTime reservationTime = storeResPosNumDTO.getReservationTime();
        int resPosNumber = storeResPosNumDTO.getResPosNumber();


        // storeNo가 없는 경우 처리
        if (storeNo == null) {
            String errorMessage = "Missing required parameter: storeNo";  // 오류 메시지를 변수로 분리
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }

        try {
            // 서비스 호출
            bossService.insertAvailableSlots(storeNo, reservationDate, reservationTime, resPosNumber);
            return ResponseEntity.ok("Available slots inserted successfully");
        } catch (Exception e) {
            String errorMessage = "Failed to insert available slots: " + e.getMessage();  // 오류 메시지를 변수로 분리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }


    // 예약 가능 인원 업데이트
    @PostMapping("/updateAvailableSlots")
    public ResponseEntity<String> updateAvailableSlots(@RequestBody StoreResPosNumDTO storeResPosNumDTO) {
        Long storeNo = storeResPosNumDTO.getStoreNo();
        LocalDate reservationDate = storeResPosNumDTO.getReservationDate();
        LocalTime reservationTime = storeResPosNumDTO.getReservationTime();
        int resPosNumber = storeResPosNumDTO.getResPosNumber();

        // 값 검증
        if (storeNo == null || reservationDate == null || reservationTime == null) {
            return ResponseEntity.badRequest().body("Missing required parameters.");
        }

        if (resPosNumber < 0) {
            return ResponseEntity.badRequest().body("Slot number cannot be negative");
        }

        try {
            // 기존 예약 가능한 슬롯을 가져와서 업데이트
            bossService.updateAvailableSlots(storeNo, reservationDate, reservationTime, resPosNumber);
            return ResponseEntity.ok("Available slots updated successfully");
        } catch (Exception e) {
            String errorMessage = "Failed to update available slots: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }





    // 최신 리뷰 받아오기
    @GetMapping("/recentReview")
    public ResponseEntity<?> getRecentReview(@ModelAttribute ReviewContentDTO reviewContentDTO, @RequestParam("storeNo") long storeNo){
        try {
            reviewContentDTO = bossService.getRecentReview(storeNo);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(reviewContentDTO);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("최신 리뷰 받아오는 도중에 에러가 발생했습니다.");
        }
    }

    // 리뷰 리스트 가져오기
    @GetMapping("/reviewList")
    public ResponseEntity<?> getReviewList(@ModelAttribute ReviewDTO reviewDTO, @RequestParam("storeNo") long storeNo) {
        try {
            reviewDTO = bossService.getReviewList(storeNo);

            System.out.println("리뷰 리스트 가져옴" + reviewDTO);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(reviewDTO);

        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("리뷰 리스트를 받아오는 도중에 에러가 발생했습니다.");
        }
    }

    // 리뷰 정보 가져오기(상세조회)
    @GetMapping("/getReviewInfo")
    public ResponseEntity<?> getReviewInfo(@RequestParam("reviewNo") Long reviewNo) {
        try{

            DetailReviewInfoDTO detailReviewInfoDTO = bossService.getReviewInfo(reviewNo);

            System.out.println("reviewContentDTO = " + detailReviewInfoDTO);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(detailReviewInfoDTO);

        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("리뷰 상세 정보를 불러오는 도중에 에러가 발생했습니다.");
        }
    }

    // 리뷰 신고
    @PostMapping("/reviewReport")
    @ResponseBody
    public ResponseEntity<?> reportReview(@RequestParam("storeNo") long storeNo, @RequestBody ReportReviewDTO reportReviewDTO){
        try{

            System.out.println("..dhdhdhdhd.." + reportReviewDTO);

            // 리뷰 신고 등록
            bossService.reportReview(reportReviewDTO);

            // 리뷰 신고 기록 +1
            bossService.updateReportCount(reportReviewDTO.getReviewNo());

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("message", "리뷰 신고 성공했습니다.");

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(responseMap);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("리뷰 신고 도중에 에러가 발생했습니다.");
        }
    }

    // 문의 내역 조회
    @GetMapping("/inquiryList")
    public ResponseEntity<?> getInquiryList(@RequestParam("storeNo") long storeNo, @RequestParam("userNo") long userNo, @RequestParam(value = "searchWord" , required = false) String searchWord){
        try{

            // INQUIRY 테이블에서 가져오기
            List<InquiryDTO> inquiryList =  bossService.getInquiryList(userNo, searchWord);

            // REVIEW_REPORT 테이블에서 가져오기
            List<InquiryDTO> reportList = bossService.getReportList(storeNo, searchWord);

            inquiryList.addAll(reportList);
            inquiryList.sort(Comparator.comparing(InquiryDTO::getInquiryDate).reversed());

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(inquiryList);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("문의 내역 불러오는 도중에 에러가 발생했습니다.");
        }

    }

    // 최근 문의 내역 조회
    @GetMapping("/recentInquiry")
    public ResponseEntity<?> getRecentInquiryList(@RequestParam("storeNo") long storeNo, @RequestParam("userNo") long userNo){
        try {
            List<InquiryDTO> RecentInquirytList = bossService.getRecentInquiryList(userNo);

            // REVIEW_REPORT 테이블에서 가져오기
            List<InquiryDTO> RecentReportList = bossService.getRecentReportList(storeNo);

            RecentInquirytList.addAll(RecentReportList);
            RecentInquirytList.sort(Comparator.comparing(InquiryDTO::getInquiryDate).reversed());

            if (RecentInquirytList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body("최근 문의 내역이 없습니다.");
            }

            // 가장 마지막 요소 가져오기
            InquiryDTO lastInquiry = RecentInquirytList.get(0);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(lastInquiry);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("최근 문의 내역 조회 도중에 에러가 발생했습니다.");
        }
    }

    @GetMapping("getSpecificInquiry")
    public ResponseEntity<?> getSpecificInquiry(@RequestParam("inquiryNo") long inquiryNo, @RequestParam("categoryNo") long categoryNo){
        try {
            InquiryDTO inquiryDTO;
            // 리뷰 신고일 경우
            if (categoryNo == 0) {
                String table = "TBL_REVIEW_REPORT";
                inquiryDTO = bossService.getInquiryInfo(inquiryNo, table);
            } else {
                String table = "TBL_INQUIRY";
                inquiryDTO = bossService.getInquiryInfo(inquiryNo, table);
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(inquiryDTO);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("문의 상세 정보를 불러오는 도중에 에러가 발생했습니다.");
        }
    }

    @DeleteMapping("/deleteInquiry")
    public ResponseEntity<?> deleteInquiry(@RequestParam("inquiryNo") long inquiryNo, @RequestParam("categoryNo") long categoryNo, @RequestParam("reviewNo") long reviewNo){
        try {
            if (categoryNo == 0) {
                // tbl_review_report에서 삭제
                bossService.deleteReviewReport(inquiryNo);
                // review에서 reportCount -1
                bossService.decreaseReportCount(reviewNo);
            } else {
                String existingFile = bossService.getFileName(inquiryNo);

                bossService.deleteInquiry(inquiryNo);

                // 여기 파일 삭제
                if (existingFile != null && !existingFile.isEmpty()) {
                    File fileToDelete = new File(INQUIRY_SHARE_DRIVE + "/" + existingFile);
                    if (fileToDelete.exists()) {
                        boolean deleted = fileToDelete.delete();
                        if (!deleted) {
                            throw new RuntimeException("파일 삭제 실패");
                        }
                    }
                }

            }
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("message", "문의 내역 삭제에 성공했습니다.");

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(responseMap);
        }  catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("문의 내역 삭제 도중에 에러가 발생했습니다.");
        }
    }

    @PutMapping(value = "/updateInquiry", consumes = "multipart/form-data")
    @ResponseBody
    public ResponseEntity<?> updateInquiry(@RequestParam("inquiryNo") long inquiryNo,
                              @RequestParam("categoryNo") long categoryNo,
                              @RequestParam("params") String params,
                              @RequestParam("reviewNo") long reviewNo,
                              @RequestPart(value = "inquiryFile", required = false) MultipartFile singleFile){

        try {

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> paramMap;
            InquiryDTO inquiryDTO = new InquiryDTO();

            System.out.println("문의 수정입니다. = " + params);

            try {
                paramMap = objectMapper.readValue(params, Map.class);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("문의 정보 파싱 오류");
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

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("message", "문의 수정에 성공했습니다.");

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(responseMap);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("문의 내역 수정 도중에 에러가 발생했습니다.");
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

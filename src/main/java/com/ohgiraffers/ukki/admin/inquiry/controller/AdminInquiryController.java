package com.ohgiraffers.ukki.admin.inquiry.controller;

import com.ohgiraffers.ukki.common.service.GoogleDriveService;
import com.ohgiraffers.ukki.inquiry.model.service.InquiryService;
import org.springframework.http.MediaType;

import com.ohgiraffers.ukki.admin.inquiry.model.dto.AnswerDTO;
import com.ohgiraffers.ukki.admin.inquiry.model.dto.AdminInquiryInfoDTO;
import com.ohgiraffers.ukki.admin.inquiry.model.dto.AdminInquiryListDTO;
import com.ohgiraffers.ukki.admin.inquiry.model.dto.AdminReportInfoDTO;
import com.ohgiraffers.ukki.admin.inquiry.model.service.AdminInquiryService;
import com.ohgiraffers.ukki.common.InquiryState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/admin/inquiries")
public class AdminInquiryController {

    private static final String INQUIRY_FOLDER_ID = "1Bzigy3LlWfu5wAj7vB5Xdp_QapW76eQG";
    private final AdminInquiryService adminInquiryService;
    private final GoogleDriveService googleDriveService;
    private final InquiryService inquiryService;

    @Autowired
    public AdminInquiryController(AdminInquiryService adminInquiryService, GoogleDriveService googleDriveService, InquiryService inquiryService){
        this.adminInquiryService = adminInquiryService;
        this.googleDriveService = googleDriveService;
        this.inquiryService = inquiryService;
    }

    @GetMapping("/processingInquiry")
    public ResponseEntity<?> processingInquiry() {
        try {
            int processingInquiryCount = adminInquiryService.processingInquiry();

            System.out.println(processingInquiryCount);

            Map<String, Integer> response = new HashMap<>();
            response.put("processing", processingInquiryCount);

            return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(response);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body("처리 중인 문의 수를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @GetMapping("/total")
    public ResponseEntity<?> totalInquiry() {
        int totalInquiry = adminInquiryService.totalInquiry();


        Map<String, Integer> response = new HashMap<>();
        response.put("totalInquiry", totalInquiry);

        return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(response);
    }

    @GetMapping("/list/user")
    public ResponseEntity<?> searchUserInquiry(@RequestParam(required = false) String category, @RequestParam(required = false) String word){
        try {
            if ("STATE".equals(category) && word != null) {
                word = InquiryState.fromValue(word).name(); // "처리완료" => "COMPLETE"로 변환 }
            }
            List<AdminInquiryListDTO> userInquiryList = adminInquiryService.searchUserInquiry(category, word);
            System.out.println(userInquiryList);

            return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(userInquiryList);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body("회원 문의를 불러오는 도중 에러가 발생했습니다.");    
        }
    }

    @GetMapping("/list/store")
    public ResponseEntity<?> searchStoreInquiry(@RequestParam(required = false) String category, @RequestParam(required = false) String word){
        try {
            if ("STATE".equals(category) && word != null) {
                word = InquiryState.fromValue(word).name(); // "처리완료" => "COMPLETE"로 변환 }
            }
            List<AdminInquiryListDTO> storeInquiryList = adminInquiryService.searchStoreInquiry(category, word);
            List<AdminInquiryListDTO> storeReportList = adminInquiryService.searchStoreReportInquiry(category, word);

            storeInquiryList.addAll(storeReportList);
            System.out.println(storeInquiryList);

            return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(storeInquiryList);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body("회원 문의를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @GetMapping("/info/{inquiryNo}")
    public ResponseEntity<?> inquiryInfo(@PathVariable int inquiryNo) {
        try {
            System.out.println("왔당");
            AdminInquiryInfoDTO inquiryInfo = adminInquiryService.inquiryInfo(inquiryNo);
            System.out.println(inquiryInfo);

            return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(inquiryInfo);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body("리뷰정보를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @GetMapping("/info/report/{reportNo}")
    public ResponseEntity<?> reportInfo(@PathVariable int reportNo) {
        try {
            System.out.println("리뷰신고왔다");
            AdminReportInfoDTO reportInfo = adminInquiryService.reportInfo(reportNo);
            System.out.println(reportInfo);

            return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportInfo);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body("리뷰정보를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @PutMapping("/info/{inquiryNo}")
    public ResponseEntity<?> inquiryAnswer(@PathVariable int inquiryNo, @RequestBody AnswerDTO answer){
        try {
            answer.setInquiryNo(inquiryNo);
            // LocalDate 대신 직접 String으로 포맷팅
            String dateString = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
            answer.setAnswerDate(dateString);
            answer.setState(InquiryState.COMPLETE);

            adminInquiryService.inquiryAnswer(answer);

            Map<String, String> response = new HashMap<>();
            response.put("message", "답변 등록 완료");

            return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body("리뷰정보를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @DeleteMapping("/info/{inquiryNo}")
    public ResponseEntity<?> inquiryDelete(@PathVariable int inquiryNo){
        try {
            AdminInquiryInfoDTO inquiryDTO = adminInquiryService.inquiryInfo(inquiryNo);
            String fileUrl = inquiryDTO.getFile();

            if(fileUrl != null && !fileUrl.isEmpty()) {
                // URL에서 fileId 추출 (https://drive.google.com/uc?id=FILE_ID 형식)
                String fileId = fileUrl.substring(fileUrl.lastIndexOf("=") + 1);
                // Google Drive에서 파일 삭제
                googleDriveService.deleteFile(fileId);
            }

            adminInquiryService.inquiryDelete(inquiryNo);

            Map<String, String> response = new HashMap<>();
            response.put("message", "문의 삭제 완료");

            return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body("문의 삭제 중 에러가 발생했습니다.");
        }
    }


    @PutMapping("/info/report/{reportNo}")
    public ResponseEntity<?> reportAnswer(@PathVariable int reportNo, @RequestBody AnswerDTO answer){
        try {
            answer.setInquiryNo(reportNo);
            // LocalDate 대신 직접 String으로 포맷팅
            String dateString = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
            answer.setAnswerDate(dateString);
            answer.setState(InquiryState.COMPLETE);

            adminInquiryService.reportAnswer(answer);

            Map<String, String> response = new HashMap<>();
            response.put("message", "답변 등록 완료");

            return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body("리뷰정보를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @DeleteMapping("/info/report/{reportNo}")
    public ResponseEntity<?> reportDelete(@PathVariable int reportNo) {
        try {
            adminInquiryService.reportDelete(reportNo);

            Map<String, String> response = new HashMap<>();
            response.put("message", "리뷰신고 삭제 완료");

            return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(response);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body("리뷰정보를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @GetMapping("/files/{fileUrl}")
    public ResponseEntity<?> getFile(@PathVariable String fileUrl) {
        try {
            // 이미 완전한 URL이므로 그대로 반환
            return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(Collections.singletonMap("url", fileUrl));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body("파일 URL을 가져오는 중 에러가 발생했습니다.");
        }
    }

    @GetMapping("/store/{userNo}")
    public ResponseEntity<?> searchStoreNoByUserNo(@PathVariable int userNo) {
        try {
            System.out.println("zjahs");
            int storeNo = adminInquiryService.searchStoreNoByUserNo(userNo);

            Map<String, Integer> response = new HashMap<>();
            response.put("storeNo", storeNo);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("가게 번호를 불러오는 도중 에러가 발생했습니다.");
        }
    }

}

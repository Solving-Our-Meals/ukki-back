package com.ohgiraffers.ukki.admin.inquiry.controller;

import com.ohgiraffers.ukki.admin.inquiry.model.dto.AnswerDTO;
import com.ohgiraffers.ukki.admin.inquiry.model.dto.InquiryInfoDTO;
import com.ohgiraffers.ukki.admin.inquiry.model.dto.InquiryListDTO;
import com.ohgiraffers.ukki.admin.inquiry.model.dto.ReportInfoDTO;
import com.ohgiraffers.ukki.admin.inquiry.model.service.AdminInquiryService;
import com.ohgiraffers.ukki.common.InquiryState;
import com.ohgiraffers.ukki.inquiry.model.dto.InquiryDTO;
import com.ohgiraffers.ukki.common.service.GoogleDriveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/admin/inquiries")
public class AdminInquiryController {

    private static final String INQUIRY_FOLDER_ID = "1Bzigy3LlWfu5wAj7vB5Xdp_QapW76eQG";
    private final AdminInquiryService adminInquiryService;
    private final GoogleDriveService googleDriveService;

    @Autowired
    public AdminInquiryController(AdminInquiryService adminInquiryService, GoogleDriveService googleDriveService){
        this.adminInquiryService = adminInquiryService;
        this.googleDriveService = googleDriveService;
    }

    @GetMapping("/processingInquiry")
    public ResponseEntity<?> processingInquiry() {
        try {
            int processingInquiryCount = adminInquiryService.processingInquiry();

            System.out.println(processingInquiryCount);

            Map<String, Integer> response = new HashMap<>();
            response.put("processing", processingInquiryCount);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("처리 중인 문의 수를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @GetMapping("/total")
    public ResponseEntity<?> totalInquiry() {
        int totalInquiry = adminInquiryService.totalInquiry();


        Map<String, Integer> response = new HashMap<>();
        response.put("totalInquiry", totalInquiry);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/list/user")
    public ResponseEntity<?> searchUserInquiry(@RequestParam(required = false) String category, @RequestParam(required = false) String word){
        try {
            if ("STATE".equals(category) && word != null) {
                word = InquiryState.fromValue(word).name(); // "처리완료" => "COMPLETE"로 변환 }
            }
            List<InquiryListDTO> userInquiryList = adminInquiryService.searchUserInquiry(category, word);

            return ResponseEntity.ok(userInquiryList);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("회원 문의를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @GetMapping("/list/store")
    public ResponseEntity<?> searchStoreInquiry(@RequestParam(required = false) String category, @RequestParam(required = false) String word){
        try {
            if ("STATE".equals(category) && word != null) {
                word = InquiryState.fromValue(word).name(); // "처리완료" => "COMPLETE"로 변환 }
            }
            List<InquiryListDTO> storeInquiryList = adminInquiryService.searchStoreInquiry(category, word);
            List<InquiryListDTO> storeReportList = adminInquiryService.searchStoreReportInquiry(category, word);

            storeInquiryList.addAll(storeReportList);

            storeInquiryList.sort(Comparator.comparing(InquiryListDTO::getInqDate));

            return ResponseEntity.ok(storeInquiryList);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("회원 문의를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @GetMapping("/info/{inquiryNo}")
    public ResponseEntity<?> inquiryInfo(@PathVariable int inquiryNo) {
        try {
            System.out.println("왔당");
            InquiryInfoDTO inquiryInfo = adminInquiryService.inquiryInfo(inquiryNo);
            System.out.println(inquiryInfo);

            return ResponseEntity.ok(inquiryInfo);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("리뷰정보를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @GetMapping("/info/report/{reportNo}")
    public ResponseEntity<?> reportInfo(@PathVariable int reportNo) {
        try {
            System.out.println("리뷰신고왔다");
            ReportInfoDTO reportInfo = adminInquiryService.reportInfo(reportNo);
            System.out.println(reportInfo);

            return ResponseEntity.ok(reportInfo);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
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

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("리뷰정보를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @DeleteMapping("/info/{inquiryNo}")
    public ResponseEntity<?> inquiryDelete(@PathVariable int inquiryNo){
        try {
            InquiryInfoDTO inquiryDTO = adminInquiryService.inquiryInfo(inquiryNo);
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

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
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

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("리뷰정보를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @DeleteMapping("/info/report/{reportNo}")
    public ResponseEntity<?> reportDelete(@PathVariable int reportNo) {
        try {
            adminInquiryService.reportDelete(reportNo);

            Map<String, String> response = new HashMap<>();
            response.put("message", "리뷰신고 삭제 완료");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("리뷰정보를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @GetMapping("/files/{fileUrl}")
    public ResponseEntity<?> getFile(@PathVariable String fileUrl) {
        try {
            // 이미 완전한 URL이므로 그대로 반환
            return ResponseEntity.ok().body(Collections.singletonMap("url", fileUrl));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("파일 URL을 가져오는 중 에러가 발생했습니다.");
        }
    }

}

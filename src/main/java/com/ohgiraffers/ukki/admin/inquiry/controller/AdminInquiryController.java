package com.ohgiraffers.ukki.admin.inquiry.controller;

import com.ohgiraffers.ukki.admin.inquiry.model.dto.AnswerDTO;
import com.ohgiraffers.ukki.admin.inquiry.model.dto.InquiryInfoDTO;
import com.ohgiraffers.ukki.admin.inquiry.model.dto.InquiryListDTO;
import com.ohgiraffers.ukki.admin.inquiry.model.dto.ReportInfoDTO;
import com.ohgiraffers.ukki.admin.inquiry.model.service.AdminInquiryService;
import com.ohgiraffers.ukki.common.InquiryState;
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

    private final String SHARED_FOLDER = "\\\\192.168.0.138\\ukki_nas\\inquiry";
    private final AdminInquiryService adminInquiryService;

    @Autowired
    public AdminInquiryController(AdminInquiryService adminInquiryService){
        this.adminInquiryService = adminInquiryService;
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
            System.out.println("수정왔당");
            answer.setInquiryNo(inquiryNo);
            LocalDate date = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String dateString = date.format(formatter);
            answer.setAnswerDate(dateString);
            answer.setState(InquiryState.COMPLETE);

            adminInquiryService.inquiryAnswer(answer);

            Map<String, String> response = new HashMap<>();
            response.put("message", "답변 등록 완료");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("리뷰정보를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @DeleteMapping("/info/{inquiryNo}")
    public ResponseEntity<?> inquiryDelete(@PathVariable int inquiryNo){
        try {
            adminInquiryService.inquiryDelete(inquiryNo);

            Map<String, String> response = new HashMap<>();
            response.put("message", "문의 삭제 완료");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("리뷰정보를 불러오는 도중 에러가 발생했습니다.");
        }
    }


    @PutMapping("/info/report/{reportNo}")
    public ResponseEntity<?> reportAnswer(@PathVariable int reportNo, @RequestBody AnswerDTO answer){
        try {
            answer.setInquiryNo(reportNo);
            LocalDate date = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String dateString = date.format(formatter);
            answer.setAnswerDate(dateString);
            answer.setState(InquiryState.COMPLETE);

            adminInquiryService.reportAnswer(answer);

            Map<String, String> response = new HashMap<>();
            response.put("message", "답변 등록 완료");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
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

    @GetMapping("/files/{filename}")
    public ResponseEntity<?> getFile(@PathVariable String filename) throws MalformedURLException {
        try {
            Path filePath = Paths.get(SHARED_FOLDER).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            String contentType = null;
//        Files.probeContentType(Path) 메소드를 사용하여 지정된 파일의 MIME 타입을 알아내기.
            try {
                contentType = Files.probeContentType(filePath);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

//        파일 타입의 추론하지 못했을 때 "application/octet-stream" -> 일반적인 바이너리 데이터 타입으로 파일의 타입이 명확하지 않을 때 사용
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        }catch (Exception e){
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("리뷰정보를 불러오는 도중 에러가 발생했습니다.");
        }
    }

}

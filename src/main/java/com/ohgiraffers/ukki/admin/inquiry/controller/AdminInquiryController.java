package com.ohgiraffers.ukki.admin.inquiry.controller;

import com.ohgiraffers.ukki.admin.inquiry.model.dto.InquiryInfoDTO;
import com.ohgiraffers.ukki.admin.inquiry.model.dto.InquiryListDTO;
import com.ohgiraffers.ukki.admin.inquiry.model.dto.ReportInfoDTO;
import com.ohgiraffers.ukki.admin.inquiry.model.service.AdminInquiryService;
import com.ohgiraffers.ukki.common.InquiryState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}

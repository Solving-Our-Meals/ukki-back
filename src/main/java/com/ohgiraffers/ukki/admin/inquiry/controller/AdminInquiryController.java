package com.ohgiraffers.ukki.admin.inquiry.controller;

import com.ohgiraffers.ukki.admin.inquiry.model.service.AdminInquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/inquiries")
public class AdminInquiryController {

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

}

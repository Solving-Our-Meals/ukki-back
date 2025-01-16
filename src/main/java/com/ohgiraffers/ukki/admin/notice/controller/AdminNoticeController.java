package com.ohgiraffers.ukki.admin.notice.controller;

import com.ohgiraffers.ukki.admin.notice.model.dto.AdminNoticeCategoryDTO;
import com.ohgiraffers.ukki.admin.notice.model.service.AdminNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ohgiraffers.ukki.admin.notice.model.dto.AdminNoticeDTO;

import java.util.List;

@RestController
@RequestMapping("/admin/notices")
public class AdminNoticeController {

    private final AdminNoticeService adminNoticeService;

    @Autowired
    public AdminNoticeController(AdminNoticeService adminNoticeService){
        this.adminNoticeService = adminNoticeService;
    }

    @GetMapping("/category")
    public ResponseEntity<?> searchNoticeCategory() {
        try {
            List<AdminNoticeCategoryDTO> noticeCategory = adminNoticeService.searchNoticeCategory();

            return ResponseEntity.ok(noticeCategory);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("회원 문의를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @GetMapping("/list/user")
    public ResponseEntity<?> searchUserNotice(@RequestParam(required = false) String word) {
        try {
            System.out.println(word);
            List<AdminNoticeDTO> userNoticeList = adminNoticeService.searchUserNotice(word);

            return ResponseEntity.ok(userNoticeList);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("회원 문의를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @GetMapping("/list/store")
    public ResponseEntity<?> searchStoreNotice(@RequestParam(required = false) String word) {
        try {
            List<AdminNoticeDTO> storeNoticeList = adminNoticeService.searchStoreNotice(word);

            return ResponseEntity.ok(storeNoticeList);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("회원 문의를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @GetMapping("/info/{noticeNo}")
    public ResponseEntity<?> searchNoticeInfo(@PathVariable int noticeNo) {
        try {
            AdminNoticeDTO noticeInfo = adminNoticeService.searchNoticeInfo(noticeNo);

            return ResponseEntity.ok(noticeInfo);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("회원 문의를 불러오는 도중 에러가 발생했습니다.");
        }
    }
}

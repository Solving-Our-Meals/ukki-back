package com.ohgiraffers.ukki.admin.notice.controller;

import com.ohgiraffers.ukki.admin.notice.model.dto.AdminNoticeCategoryDTO;
import com.ohgiraffers.ukki.admin.notice.model.service.AdminNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ohgiraffers.ukki.admin.notice.model.dto.AdminNoticeDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;

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

            return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(noticeCategory);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
                    .body("공지사항 카테고리를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @GetMapping("/list/user")
    public ResponseEntity<?> searchUserNotice(@RequestParam(required = false) String word) {
        try {
            List<AdminNoticeDTO> userNoticeList = adminNoticeService.searchUserNotice(word);

            return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(userNoticeList);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body("회원 공지사항을 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @GetMapping("/list/store")
    public ResponseEntity<?> searchStoreNotice(@RequestParam(required = false) String word) {
        try {
            List<AdminNoticeDTO> storeNoticeList = adminNoticeService.searchStoreNotice(word);

            return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(storeNoticeList);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body("가게 공지사항을 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @GetMapping("/info/{noticeNo}")
    public ResponseEntity<?> searchNoticeInfo(@PathVariable int noticeNo) {
        try {
            AdminNoticeDTO noticeInfo = adminNoticeService.searchNoticeInfo(noticeNo);

            return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(noticeInfo);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body("공지사항 정보를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @PutMapping("/info/{noticeNo}")
    public ResponseEntity<?> editNoticeInfo(@PathVariable int noticeNo, @RequestBody AdminNoticeDTO adminNoticeDTO) {
        try {
            adminNoticeDTO.setNoticeNo(noticeNo);

            adminNoticeService.editNoticeInfo(adminNoticeDTO);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("message", "수정에 성공했습니다.");

            return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(responseMap);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body("공지사항 수정 중 에러가 발생했습니다.");
        }
    }

    @DeleteMapping("/info/{noticeNo}")
    public ResponseEntity<?> deleteNotice(@PathVariable int noticeNo) {
        try{
            adminNoticeService.deleteNotice(noticeNo);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("message", "삭제에 성공했습니다.");

            return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(responseMap);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body("공지사항 삭제 중 에러가 발생했습니다.");
        }
    }

    @PostMapping("/regist")
    public ResponseEntity<?> registNotice(@RequestBody AdminNoticeDTO adminNoticeDTO) {
        try{
            adminNoticeService.registNotice(adminNoticeDTO);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("message", "등록에 성공했습니다.");

            return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(responseMap);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body("공지사항 등록 중 에러가 발생했습니다.");
        }
    }
}

package com.ohgiraffers.ukki.admin.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohgiraffers.ukki.admin.review.model.dto.AdminReviewInfoDTO;
import com.ohgiraffers.ukki.admin.review.model.dto.AdminReviewListDTO;
import com.ohgiraffers.ukki.admin.review.model.service.AdminReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ohgiraffers.ukki.common.service.GoogleDriveService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;

@RestController
@RequestMapping("/admin/reviews")
public class AdminReviewController {

    private final GoogleDriveService googleDriveService;
    private final AdminReviewService adminReviewService;

    @Autowired
    public AdminReviewController(AdminReviewService adminReviewService, GoogleDriveService googleDriveService){
        this.adminReviewService = adminReviewService;
        this.googleDriveService = googleDriveService;
    }

    @GetMapping("/total")
    public ResponseEntity<?> totalReview(){
        try {
            int total = adminReviewService.totalReview();

            Map<String, Integer> response = new HashMap<>();

            response.put("totalReview", total);

            return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(response);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
                    .body("총 제휴가게 수 정보를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @GetMapping("list")
    public ResponseEntity<?> searchReview(@RequestParam(required = false) String category, @RequestParam(required = false) String word){
        try {
            List<AdminReviewListDTO> reviewList = adminReviewService.searchReview(category, word);
            System.out.println(reviewList);

            return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(reviewList);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body("리뷰리스트를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @GetMapping("info/{reviewNo}")
    public ResponseEntity<?> searchReviewInfo(@PathVariable String reviewNo){
        try {
            AdminReviewInfoDTO reviewInfo = adminReviewService.searchReviewInfo(reviewNo);
            System.out.println(reviewInfo);

            return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(reviewInfo);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body("리뷰정보를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @DeleteMapping("info/{reviewNo}")
    public ResponseEntity<?> deleteReview(@PathVariable int reviewNo, @RequestBody String content){
        try {
            ObjectMapper mapper = new ObjectMapper();
            System.out.println("삭제왔당");
            String reviewImg = mapper.readTree(content).get("reviewImg").asText();
            System.out.println(reviewImg);

            int result = adminReviewService.deleteReview(reviewNo);


            if(result > 0){
                if(!reviewImg.equals("DEFAULT_REVIEW_IMG.png")) {
                    googleDriveService.deleteFile(reviewImg);
                }
            }

            Map<String, String> response = new HashMap<>();
            response.put("message", "삭제 성공");

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
}

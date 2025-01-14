package com.ohgiraffers.ukki.admin.review.controller;

import com.ohgiraffers.ukki.admin.review.model.dto.ReviewInfoDTO;
import com.ohgiraffers.ukki.admin.review.model.dto.ReviewListDTO;
import com.ohgiraffers.ukki.admin.review.model.service.AdminReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/reviews")
public class AdminReviewController {

    private final AdminReviewService adminReviewService;

    @Autowired
    public AdminReviewController(AdminReviewService adminReviewService){
        this.adminReviewService = adminReviewService;
    }

    @GetMapping("/total")
    public ResponseEntity<?> totalReview(){
        try {
            int total = adminReviewService.totalReview();

            Map<String, Integer> response = new HashMap<>();

            response.put("totalReview", total);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("총 제휴가게 수 정보를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @GetMapping("list")
    public ResponseEntity<?> searchReview(@RequestParam(required = false) String category, @RequestParam(required = false) String word){
        try {
            List<ReviewListDTO> reviewList = adminReviewService.searchReview(category, word);
            System.out.println(reviewList);

            return ResponseEntity.ok(reviewList);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("리뷰리스트를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @GetMapping("info/{reviewNo}")
    public ResponseEntity<?> searchReviewInfo(@PathVariable String reviewNo){
        try {
            ReviewInfoDTO reviewInfo = adminReviewService.searchReviewInfo(reviewNo);
            System.out.println(reviewInfo);

            return ResponseEntity.ok(reviewInfo);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("리뷰정보를 불러오는 도중 에러가 발생했습니다.");
        }
    }

}

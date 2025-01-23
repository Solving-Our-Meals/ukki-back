package com.ohgiraffers.ukki.admin.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohgiraffers.ukki.admin.review.model.dto.ReviewInfoDTO;
import com.ohgiraffers.ukki.admin.review.model.dto.ReviewListDTO;
import com.ohgiraffers.ukki.admin.review.model.service.AdminReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;

@RestController
@RequestMapping("/admin/reviews")
public class AdminReviewController {

    private final String SHARED_FOLDER = "\\\\I7E-74\\ukki_nas\\store";
//    private final String SHARED_FOLDER = "\\\\DESKTOP-KLQ0O04\\Users\\admin\\Desktop\\ukkiImg";

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
            List<ReviewListDTO> reviewList = adminReviewService.searchReview(category, word);
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
            ReviewInfoDTO reviewInfo = adminReviewService.searchReviewInfo(reviewNo);
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
            String reviewImg = mapper.readTree(content).get("reviewImg").asText()+".png";

            int result = adminReviewService.deleteReview(reviewNo);


            if(result > 0){
                if(!reviewImg.equals("DEFAULT_REVIEW_IMG.png")) {
                    System.out.println("리뷰이미지 삭제 왔당");
                    Path filePathProfile = Paths.get(SHARED_FOLDER, reviewImg);
                    Files.deleteIfExists(filePathProfile);
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

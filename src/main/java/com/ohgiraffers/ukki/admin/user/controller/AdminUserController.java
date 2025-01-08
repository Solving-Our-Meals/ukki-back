package com.ohgiraffers.ukki.admin.user.controller;

import com.ohgiraffers.ukki.admin.reservation.model.service.AdminReservationService;
import com.ohgiraffers.ukki.admin.review.model.service.AdminReviewService;
import com.ohgiraffers.ukki.admin.user.model.dto.*;
import com.ohgiraffers.ukki.admin.user.model.service.AdminUserService;
import com.ohgiraffers.ukki.inquiry.model.dto.InquiryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/users")
public class AdminUserController {

    private final AdminUserService adminUserService;
    private final AdminReservationService adminReservationService;
    private final AdminReviewService adminReviewService;

    @Autowired
    public AdminUserController(AdminUserService adminUserService , AdminReservationService adminReservationService , AdminReviewService adminReviewService) {
        this.adminUserService = adminUserService;
        this.adminReservationService = adminReservationService;
        this.adminReviewService = adminReviewService;
    }

    @GetMapping("/actinfo")
    public ResponseEntity<?> actinfo() {
                // 가정: resList와 reviewList가 이미 채워져 있음
                List<AdminUserResDTO> resList = adminReservationService.userResList();
                List<AdminUserReviewDTO> reviewList = adminReviewService.userReviewList();
                List<AdminUserActInfoDTO> actInfoList = new ArrayList<>();

                // userNo를 키로 사용하여 AdminUserActInfoDTO를 저장할 Map
                Map<Integer, AdminUserActInfoDTO> userActInfoMap = new HashMap<>();

                // resList의 데이터를 userActInfoMap에 추가
                for (AdminUserResDTO res : resList) {
                    int userNo = res.getUserNo();
                    int totalRes = res.getTotalRes();
                    int totalRandomRes = res.getTotalRandomRes();

                    if (userActInfoMap.containsKey(userNo)) {
                        AdminUserActInfoDTO actInfo = userActInfoMap.get(userNo);
                        actInfo.setResCount(actInfo.getResCount() + totalRes);
                        actInfo.setRandomCount(actInfo.getRandomCount() + totalRandomRes);
                    } else {
                        userActInfoMap.put(userNo, new AdminUserActInfoDTO(userNo, totalRes, 0, totalRandomRes));
                    }
                }

                // reviewList의 데이터를 userActInfoMap에 추가
                for (AdminUserReviewDTO review : reviewList) {
                    int userNo = review.getUserNo();
                    int totalReview = review.getTotalReview();

                    if (userActInfoMap.containsKey(userNo)) {
                        AdminUserActInfoDTO actInfo = userActInfoMap.get(userNo);
                        actInfo.setReviewCount(actInfo.getReviewCount() + totalReview);
                    } else {
                        userActInfoMap.put(userNo, new AdminUserActInfoDTO(userNo, 0, totalReview, 0));
                    }
                }

                // userActInfoMap의 모든 값을 actInfoList에 추가
                actInfoList.addAll(userActInfoMap.values());

                int result = adminUserService.insertActInfo(actInfoList);

        return ResponseEntity.ok("hi");
    }
    
    @GetMapping("/list")
    public ResponseEntity<?> searchAllUsers(@RequestParam(required = false) String category, @RequestParam(required = false) String word) {
        try {
            List<AdminUserDTO> userList = adminUserService.searchUsers(category, word);
            return ResponseEntity.ok(userList);
        } catch (Exception e) {
            String errorMessage = "Error occurred while retrieving users: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    @GetMapping("/info/{userNo}")
    public ResponseEntity<?> searchUserInfo(@PathVariable int userNo){
        AdminUserInfoDTO userInfoDTO = adminUserService.searchUserInfo(userNo);

        return ResponseEntity.ok(userInfoDTO);
    }

    @PutMapping("/info/{userNo}")
    public ResponseEntity<?> updateUserName(@PathVariable int userNo){
        String lastSaneName = adminUserService.searchSaneName();
        if(lastSaneName == null){
            lastSaneName = "건전한우끼0";
        }
        String saneNoPart = lastSaneName.substring(5);

        int saneNo = Integer.parseInt(saneNoPart)+1;

        String newSaneName = "건전한우끼"+saneNo;

        System.out.println(saneNo);

        int result = adminUserService.updateUserName(userNo, newSaneName);
        Map<String, Object> responseMap = new HashMap<>();
        if (result > 0) {
            responseMap.put("message", "닉네임 변경에 성공했습니다.");
            responseMap.put("success", true);
        } else {
            responseMap.put("message", "닉네임 변경에 실패했습니다.");
            responseMap.put("success", false);
        } return ResponseEntity.ok(responseMap);
    }

    @DeleteMapping("/info/{userNo}")
    public ResponseEntity<?> deleteUserInfo(@PathVariable int userNo){
        int result = adminUserService.deleteUserInfo(userNo);
        Map<String, Object> responseMap = new HashMap<>();
        if (result > 0) {
            responseMap.put("message", "회원 삭제에 성공했습니다.");
            responseMap.put("success", true);
        } else {
            responseMap.put("message", "회원 삭제에 실패했습니다.");
            responseMap.put("success", false);
        } return ResponseEntity.ok(responseMap);
    }
}

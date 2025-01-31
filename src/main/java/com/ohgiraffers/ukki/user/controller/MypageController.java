package com.ohgiraffers.ukki.user.controller;

import com.ohgiraffers.ukki.auth.model.service.JwtService;
import com.ohgiraffers.ukki.common.InquiryState;
import com.ohgiraffers.ukki.common.service.GoogleDriveService;
import com.ohgiraffers.ukki.user.model.dto.*;
import com.ohgiraffers.ukki.user.model.service.CookieService;
import com.ohgiraffers.ukki.user.model.service.MypageService;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/user/mypage")
public class MypageController {

    @Value("${google.drive.inquiry-folder-id}")
    private String Inquiry_FOLDER_ID;

    private final JwtService jwtService;
    private MypageService mypageService;
    private CookieService cookieService;
    private GoogleDriveService googleDriveService;

    @Autowired
    public MypageController (MypageService mypageService, CookieService cookieService, JwtService jwtService, GoogleDriveService googleDriveService) {
        this.mypageService = mypageService;
        this.cookieService = cookieService;
        this.jwtService = jwtService;
        this.googleDriveService = googleDriveService;
    }

    @GetMapping("/reservation")
    public ResponseEntity<List<MypageReservationDTO>> getUserReservation(
            HttpServletRequest request,
            @RequestParam(value = "search", required = false) String search) {

        String jwtToken = cookieService.getJWTCookie(request);
        if (jwtToken == null) {
            throw new IllegalArgumentException("토큰이 일치하지 않음");
        }

        String userId = jwtService.getUserInfoFromTokenId(jwtToken);
        if (userId == null) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        List<MypageReservationDTO> reservations;
        if (search != null && !search.isEmpty()) {
            reservations = mypageService.getUserReservationFromTokenWithSearch(jwtToken, userId, search);
        } else {
            reservations = mypageService.getUserReservationFromToken(jwtToken, userId);
        }

        if (reservations.isEmpty()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ArrayList<>());
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(reservations);
    }


    @GetMapping("/reservation/{resNo}")
    public ResponseEntity<MypageReservationDetailDTO> getReservationDetail(@PathVariable int resNo, HttpServletRequest request) {
        String jwtToken = cookieService.getJWTCookie(request);

        if (jwtToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(null);
        }

        String userId = jwtService.getUserInfoFromTokenId(jwtToken);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(null);
        }

        MypageReservationDetailDTO reservationDetail = mypageService.getReservationDetail(resNo, userId, jwtToken);

        if (reservationDetail == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(null);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(reservationDetail);
    }


    @GetMapping("/review")
    public ResponseEntity<List<MypageReviewDTO>> getUserReview(
            HttpServletRequest request,
            @RequestParam(value = "search", required = false) String search) {

        String jwtToken = cookieService.getJWTCookie(request);

        if (jwtToken == null) {
            throw new IllegalArgumentException("토큰이 일치하지 않음");
        }

        String userId = jwtService.getUserInfoFromTokenId(jwtToken);

        if (userId == null) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        List<MypageReviewDTO> reviews;
        if (search != null && !search.isEmpty()) {
            reviews = mypageService.getUserReviewFromTokenWithSearch(jwtToken, userId, search);
        } else {
            reviews = mypageService.getUserReviewFromToken(jwtToken, userId);
        }

        if (reviews.isEmpty()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ArrayList<>());
        }
        System.out.println(reviews);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(reviews);
    }


    @GetMapping("/review/{reviewNo}")
    public ResponseEntity<MypageReviewDTO> getUserReviewDetail(@PathVariable Long reviewNo, HttpServletRequest request) {
        String jwtToken = cookieService.getJWTCookie(request);

        if (jwtToken == null) {
            throw new IllegalArgumentException("토큰이 일치하지 않음");
        }

        String userId = jwtService.getUserInfoFromTokenId(jwtToken);

        if (userId == null) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        MypageReviewDTO reviewDetail = mypageService.getUserReviewDetailFromToken(jwtToken, userId, reviewNo);

        if (reviewDetail == null) {
            // 2025-01-23
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(reviewDetail);
    }

    @DeleteMapping("/review/delete")
    public ResponseEntity<String> deleteReview(@RequestBody MypageReviewDTO mypageReviewDTO) {
        int reviewNo = mypageReviewDTO.getReviewNo();

        boolean deleted = mypageService.deleteReview(reviewNo);

        if (deleted) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("리뷰가 삭제되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("리뷰 삭제에 실패했습니다.");
        }
    }

    @GetMapping("/inquiry")
    public ResponseEntity<List<MypageInquiryDTO>> getUseInquiry(
            HttpServletRequest request,
            @RequestParam(value = "search", required = false) String search) {
        String jwtToken = cookieService.getJWTCookie(request);

        if (jwtToken == null) {
            throw new IllegalArgumentException("토큰이 일치하지 않음");
        }

        String userId = jwtService.getUserInfoFromTokenId(jwtToken);

        if (userId == null) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        List<MypageInquiryDTO> inquiry;
        if (search != null && !search.isEmpty()) {
            inquiry = mypageService.getUserInquiryFromTokenWithSerach(jwtToken, userId, search);
        } else {
            inquiry = mypageService.getUserInquiryFromToken(jwtToken, userId);
        }

        if (inquiry.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(inquiry);
    }


    @PutMapping("/inquiry/{inquiryNo}/status")
    public ResponseEntity<Map<String, String>> updateInquiryStatusToRead(
            @PathVariable int inquiryNo, HttpServletRequest request) {

        String jwtToken = cookieService.getJWTCookie(request);
        if (jwtToken == null) {
            throw new IllegalArgumentException("토큰이 일치하지 않음");
        }

        String userId = jwtService.getUserInfoFromTokenId(jwtToken);
        if (userId == null) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        List<MypageInquiryDTO> inquiries = mypageService.getUserInquiryFromToken(jwtToken, userId);

        MypageInquiryDTO inquiry = inquiries.stream()
                .filter(i -> i.getInquiryNo() == inquiryNo)
                .findFirst()
                .orElse(null);

        if (inquiry == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Collections.singletonMap("message", "해당 문의를 찾을 수 없습니다."));
        }

        if (inquiry.getInquiryState() == InquiryState.CHECK) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Collections.singletonMap("message", "이미 문의는 '읽음' 상태입니다."));
        }

        if (inquiry.getAnswerDate() != null && inquiry.getInquiryState() != InquiryState.CHECK) {
            boolean updated = mypageService.updateInquiryStatus(inquiryNo, InquiryState.CHECK);
            if (updated) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Collections.singletonMap("message", "문의 상태가 '읽음'으로 업데이트되었습니다."));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Collections.singletonMap("message", "상태 업데이트에 실패했습니다."));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Collections.singletonMap("message", "답변이 없습니다."));
        }
    }

    @PutMapping(value = "/inquiry/{inquiryNo}", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, String>> updateInquiry(
            @PathVariable int inquiryNo,
            @RequestParam String title,       // 제목
            @RequestParam String text,        // 내용
            @RequestParam(required = false) MultipartFile file, // 파일 (선택사항)
            HttpServletRequest request) {

        System.out.println(file);

        String jwtToken = cookieService.getJWTCookie(request);
        if (jwtToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Collections.singletonMap("message", "토큰이 일치하지 않음"));
        }

        String userId = jwtService.getUserInfoFromTokenId(jwtToken);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Collections.singletonMap("message", "유효하지 않은 토큰입니다."));
        }

        List<MypageInquiryDTO> inquiries = mypageService.getUserInquiryFromToken(jwtToken, userId);

        MypageInquiryDTO inquiryToUpdate = inquiries.stream()
                .filter(i -> i.getInquiryNo() == inquiryNo)
                .findFirst()
                .orElse(null);

        if (inquiryToUpdate == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "해당 문의를 찾을 수 없습니다."));
        }

        inquiryToUpdate.setTitle(title);
        inquiryToUpdate.setText(text);

        System.out.println(title);
        System.out.println(text);

        if (file != null && !file.isEmpty()) {
            try {
                boolean updated = mypageService.updateInquiry(inquiryToUpdate, file, userId);
                if (!updated) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(Collections.singletonMap("message", "문의 수정에 실패했습니다."));
                }
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Collections.singletonMap("message", "파일 업로드 실패: " + e.getMessage()));
            }
        } else {
            boolean updated = mypageService.updateInquiry(inquiryToUpdate, null, userId);  // null로 파일을 넘겨 파일 업데이트를 건너뛰고 내용만 수정
            if (!updated) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Collections.singletonMap("message", "문의 수정에 실패했습니다."));
            }
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Collections.singletonMap("message", "문의가 성공적으로 수정되었습니다."));
    }

    @DeleteMapping("/inquiry/{inquiryNo}")
    public ResponseEntity<Map<String, String>> deleteInquiry(
            @PathVariable int inquiryNo,
            HttpServletRequest request) {

        String jwtToken = cookieService.getJWTCookie(request);
        if (jwtToken == null) {
            throw new IllegalArgumentException("토큰이 일치하지 않음");
        }

        String userId = jwtService.getUserInfoFromTokenId(jwtToken);
        if (userId == null) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        List<MypageInquiryDTO> inquiries = mypageService.getUserInquiryFromToken(jwtToken, userId);

        MypageInquiryDTO inquiryToDelete = inquiries.stream()
                .filter(i -> i.getInquiryNo() == inquiryNo)
                .findFirst()
                .orElse(null);

        if (inquiryToDelete == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Collections.singletonMap("message", "해당 문의를 찾을 수 없습니다."));
        }

        boolean deleted = mypageService.deleteInquiry(inquiryNo);

        if (deleted) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Collections.singletonMap("message", "문의가 성공적으로 삭제되었습니다."));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Collections.singletonMap("message", "문의 삭제에 실패했습니다."));
        }
    }


/*  없어도 됨 (프론트에서 아이디로 직접 다운로드 요청함)
    @GetMapping("/download/{fileId}")
    public ResponseEntity<String> downloadFile(@PathVariable String fileId) {
        try {
            // 파일 다운로드 URL을 생성
            String fileDownloadUrl = googleDriveService.getFileUrl(fileId);

            // URL을 바로 응답으로 반환 (사용자는 이 URL을 통해 파일을 다운로드)
            return ResponseEntity.ok(fileDownloadUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("파일 다운로드 중 오류가 발생했습니다.");
        }
    }*/

    @PostMapping("/confirm")
    public ResponseEntity<Map<String, String>> confirmPassword(
            @RequestBody Map<String, String> requestData, HttpServletRequest request) {

        String password = requestData.get("password");

        String jwtToken = cookieService.getJWTCookie(request);
        if (jwtToken == null || jwtService.getUserInfoFromTokenId(jwtToken) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Collections.singletonMap("message", "유효하지 않은 토큰입니다."));
        }

        String userId = jwtService.getUserInfoFromTokenId(jwtToken);
        if (mypageService.verifyPassword(userId, password)) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Collections.singletonMap("message", "비밀번호가 확인되었습니다."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Collections.singletonMap("message", "비밀번호가 일치하지 않습니다."));
        }
    }

    @GetMapping("/profile-info")
    public ResponseEntity<MypageInfoDTO> getUserInfo(HttpServletRequest request) {
        String jwtToken = cookieService.getJWTCookie(request);

        if (jwtToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        }

        String userId = jwtService.getUserInfoFromTokenId(jwtToken);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        }

        MypageInfoDTO userInfo = mypageService.getUserInfo(userId);

        if (userInfo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userInfo);
    }

    @PutMapping("/update")
    public ResponseEntity<Map<String, String>> updateUserInfo(
            @RequestBody MypageUpdateUserInfoDTO updateUserInfoDTO,
            HttpServletRequest request) {

        // 디버깅용 로그 출력
            // DTO가 null이면 예외 발생
            if (updateUserInfoDTO == null) {
                System.out.println("updateUserInfoDTO is null");
            } else {
                System.out.println(updateUserInfoDTO);
            }

        String jwtToken = cookieService.getJWTCookie(request);
        if (jwtToken == null || jwtService.getUserInfoFromTokenId(jwtToken) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Collections.singletonMap("message", "유효하지 않은 토큰입니다."));
        }

        String userId = jwtService.getUserInfoFromTokenId(jwtToken);

        try {
            boolean updated = mypageService.updateUserInfo(userId, updateUserInfoDTO.getUserName(), updateUserInfoDTO.getUserPass());

            if (updated) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Collections.singletonMap("message", "정보가 성공적으로 업데이트되었습니다."));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("message", "정보 업데이트에 실패했습니다."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "아무것도 변경하지 않았습니다."));
        }
    }


    @PostMapping("/profile-image")
    public ResponseEntity<String> uploadProfileImage(HttpServletRequest request,
                                                     @RequestParam("profileImage") MultipartFile profileImage) {
        try {
            String jwtToken = cookieService.getJWTCookie(request);
            if (jwtToken == null || !jwtService.validateToken(jwtToken)) {
                return ResponseEntity.status(401).body("유효하지 않은 토큰입니다.");
            }

            Map<String, Object> userInfo = jwtService.getUserInfoFromToken(jwtToken);
            String userId = (String) userInfo.get("userId");

            boolean result = mypageService.updateProfileImage(userId, profileImage);
            if (result) {
                // 프로필 이미지 업데이트 성공 시 응답에 메시지 포함
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"message\": \"프로필 이미지가 성공적으로 업데이트되었습니다.\"}");
            } else {
                // 프로필 이미지 업데이트 실패 시 응답에 메시지 포함
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("{\"message\": \"프로필 이미지 업데이트에 실패했습니다.\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"이미지 업로드 중 오류가 발생했습니다.\"}");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(HttpServletRequest request) {
        String jwtToken = cookieService.getJWTCookie(request);

        if (jwtToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 유효하지 않습니다.");
        }

        String userId = jwtService.getUserInfoFromTokenId(jwtToken);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
        }

        try {
            boolean deleted = mypageService.deleteUser(userId);

            if (deleted) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("회원 탈퇴가 완료되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("회원 탈퇴 처리 중 오류가 발생했습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("탈퇴 처리 중 오류가 발생했습니다.");
        }
    }

    // 예약 취소 (삭제) -> POST 매핑은 예약 취소 내역이 남을 때 만들고 우리는 기획을 완전제거로 하여 딜리트 매핑
    // 여긴 보안 뺌
    @DeleteMapping("/reservation/{resNo}")
    public ResponseEntity<Map<String, String>> cancelReservation(@PathVariable Long resNo) {
        boolean isCancelled = mypageService.deleteReservation(resNo); // 예약 취소 로직

        if (isCancelled) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "예약이 취소되었습니다.");
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);  // 200 OK와 함께 JSON 응답 반환
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "예약 취소에 실패했습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);  // 400 Bad Request와 함께 JSON 응답 반환
        }
    }

}

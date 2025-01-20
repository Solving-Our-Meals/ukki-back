package com.ohgiraffers.ukki.user.controller;

import com.ohgiraffers.ukki.auth.model.service.JwtService;
import com.ohgiraffers.ukki.common.InquiryState;
import com.ohgiraffers.ukki.user.model.dto.*;
import com.ohgiraffers.ukki.user.model.service.CookieService;
import com.ohgiraffers.ukki.user.model.service.MypageService;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/mypage")
public class MypageController {

    private final JwtService jwtService;
    private MypageService mypageService;
    private CookieService cookieService;

    @Autowired
    public MypageController (MypageService mypageService, CookieService cookieService, JwtService jwtService) {
        this.mypageService = mypageService;
        this.cookieService = cookieService;
        this.jwtService = jwtService;
    }

/*     해당 방법은 헤더로 보냈을 때 사용하는 방식으로 우리는 헤더 방식이 아닙니다. 참고해주세요 !
    @GetMapping("/{userId}")
    public MypageDTO getUserInfo(@PathVariable String userId, @RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");
        return mypageService.getUserInfoFromToken(jwtToken, userId);
    }

     쿠키로만 보냈을 땐 이 방법을 사용해야하고 우리는 쿠키로 검증하는 방식을 사용할겁니다. 쿠키서비스에 getJWTCookie 메소드를 통해 검증하시고 사용하시면 됩니다 !
     마이페이지 프로필의 닉네임을 불러오는 컨트롤러
    @GetMapping("/{userId}")
    public MypageDTO getUserInfo(@PathVariable String userId, HttpServletRequest request) {
        String jwtToken = cookieService.getJWTCookie(request);

        if (jwtToken == null) {
            throw new IllegalArgumentException("토큰 일치 안함");
        }

        return mypageService.getUserInfoFromToken(jwtToken, userId);
    }*/


    @GetMapping("/reservation")
    public ResponseEntity<List<MypageReservationDTO>> getUserReservation(HttpServletRequest request) {
        String jwtToken = cookieService.getJWTCookie(request);

        if (jwtToken == null) {
            throw new IllegalArgumentException("토큰이 일치하지 않음");
        }

        String userId = jwtService.getUserInfoFromTokenId(jwtToken);

        if (userId == null) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        List<MypageReservationDTO> reservations = mypageService.getUserReservationFromToken(jwtToken, userId);

        if (reservations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(reservations);
    }

/*    @GetMapping("/reservation/{resNo}")
    public ResponseEntity<MypageReservationDTO> getReservationDetail(@PathVariable Long resNo, HttpServletRequest request) {
        // JWT 토큰 가져오기
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

        MypageReservationDTO reservationDetail = mypageService.getReservationDetail(resNo, userId);

        if (reservationDetail == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null); // 예약 정보를 찾을 수 없을 경우
        }

        return ResponseEntity.ok(reservationDetail); // 예약 정보 반환
    }*/

    @GetMapping("/review")
    public ResponseEntity<List<MypageReviewDTO>> getUseReview(HttpServletRequest request) {
        String jwtToken = cookieService.getJWTCookie(request);

        if (jwtToken == null) {
            throw new IllegalArgumentException("토큰이 일치하지 않음");
        }

        String userId = jwtService.getUserInfoFromTokenId(jwtToken);

        if (userId == null) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        List<MypageReviewDTO> reservations = mypageService.getUserReviewFromToken(jwtToken, userId);

        if (reservations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(reservations);
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
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(reviewDetail);
    }



    @DeleteMapping("/review/delete")
    public ResponseEntity<String> deleteReview(@RequestBody MypageReviewDTO mypageReviewDTO) {
        int reviewNo = mypageReviewDTO.getReviewNo();

        boolean deleted = mypageService.deleteReview(reviewNo);

        if (deleted) {
            return ResponseEntity.ok("리뷰가 삭제되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("리뷰 삭제에 실패했습니다.");
        }
    }

    @GetMapping("/inquiry")
    public ResponseEntity<List<MypageInquiryDTO>> getUseInquiry(HttpServletRequest request) {
        String jwtToken = cookieService.getJWTCookie(request);

        if (jwtToken == null) {
            throw new IllegalArgumentException("토큰이 일치하지 않음");
        }

        String userId = jwtService.getUserInfoFromTokenId(jwtToken);

        if (userId == null) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        List<MypageInquiryDTO> inquiry = mypageService.getUserInquiryFromToken(jwtToken, userId);

        if (inquiry.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(inquiry);
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
                    .body(Collections.singletonMap("message", "해당 문의를 찾을 수 없습니다."));
        }

        if (inquiry.getInquiryState() == InquiryState.CHECK) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "이미 문의는 '읽음' 상태입니다."));
        }

        if (inquiry.getAnswerDate() != null && inquiry.getInquiryState() != InquiryState.CHECK) {
            boolean updated = mypageService.updateInquiryStatus(inquiryNo, InquiryState.CHECK);
            if (updated) {
                return ResponseEntity.ok(Collections.singletonMap("message", "문의 상태가 '읽음'으로 업데이트되었습니다."));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Collections.singletonMap("message", "상태 업데이트에 실패했습니다."));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
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

        // JWT 토큰 검사
        String jwtToken = cookieService.getJWTCookie(request);
        if (jwtToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", "토큰이 일치하지 않음"));
        }

        String userId = jwtService.getUserInfoFromTokenId(jwtToken);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", "유효하지 않은 토큰입니다."));
        }

        // 사용자 문의 정보 가져오기
        List<MypageInquiryDTO> inquiries = mypageService.getUserInquiryFromToken(jwtToken, userId);

        System.out.println("zz");

        // 수정할 문의 찾기
        MypageInquiryDTO inquiryToUpdate = inquiries.stream()
                .filter(i -> i.getInquiryNo() == inquiryNo)
                .findFirst()
                .orElse(null);

        if (inquiryToUpdate == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "해당 문의를 찾을 수 없습니다."));
        }

        // 제목과 내용 수정
        inquiryToUpdate.setTitle(title);
        inquiryToUpdate.setText(text);

        // 파일 업로드 처리 (파일이 있을 때만)
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
        }

        return ResponseEntity.ok(Collections.singletonMap("message", "문의가 성공적으로 수정되었습니다."));
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
                    .body(Collections.singletonMap("message", "해당 문의를 찾을 수 없습니다."));
        }

        boolean deleted = mypageService.deleteInquiry(inquiryNo);

        if (deleted) {
            return ResponseEntity.ok(Collections.singletonMap("message", "문의가 성공적으로 삭제되었습니다."));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "문의 삭제에 실패했습니다."));
        }
    }

    // 스프링 프레임워크 리소스 사용 ! // 서비스까지 갈 필요 없는 구간이라 서비스 부분은 제거했습니다.
    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        try {
            // 파일 경로 설정 (파일 경로는 서버상의 경로로 지정)
            Path path = Paths.get("C:/Temp/inquiry/").resolve(fileName);
            Resource resource = new FileSystemResource(path);

            // 파일이 존재하는지 확인
            if (!resource.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            // 다운로드 응답 설정
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/confirm")
    public ResponseEntity<Map<String, String>> confirmPassword(
            @RequestBody Map<String, String> requestData, HttpServletRequest request) {

        String password = requestData.get("password");

        String jwtToken = cookieService.getJWTCookie(request);
        if (jwtToken == null || jwtService.getUserInfoFromTokenId(jwtToken) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", "유효하지 않은 토큰입니다."));
        }

        String userId = jwtService.getUserInfoFromTokenId(jwtToken);
        if (mypageService.verifyPassword(userId, password)) {
            return ResponseEntity.ok(Collections.singletonMap("message", "비밀번호가 확인되었습니다."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
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

        return ResponseEntity.ok(userInfo);
    }

    @PutMapping("/update")
    public ResponseEntity<Map<String, String>> updateUserInfo(
            @RequestParam(required = false) MultipartFile profileImage,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String userPass,
            HttpServletRequest request) {

        String jwtToken = cookieService.getJWTCookie(request);
        if (jwtToken == null || jwtService.getUserInfoFromTokenId(jwtToken) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", "유효하지 않은 토큰입니다."));
        }

        String userId = jwtService.getUserInfoFromTokenId(jwtToken);

        try {
            boolean updated = mypageService.updateUserInfo(userId, userName, userPass, profileImage);

            if (updated) {
                return ResponseEntity.ok(Collections.singletonMap("message", "정보가 성공적으로 업데이트되었습니다."));
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
                return ResponseEntity.ok("프로필 이미지가 성공적으로 업데이트되었습니다.");
            } else {
                return ResponseEntity.status(500).body("프로필 이미지 업데이트에 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("이미지 업로드 중 오류가 발생했습니다.");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(HttpServletRequest request) {
        String jwtToken = cookieService.getJWTCookie(request); // 쿠키에서 JWT 토큰을 가져옵니다.

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
                return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("회원 탈퇴 처리 중 오류가 발생했습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("탈퇴 처리 중 오류가 발생했습니다.");
        }
    }

}

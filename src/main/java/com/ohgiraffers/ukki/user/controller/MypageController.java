package com.ohgiraffers.ukki.user.controller;

import com.ohgiraffers.ukki.auth.model.service.JwtService;
import com.ohgiraffers.ukki.common.InquiryState;
import com.ohgiraffers.ukki.user.model.dto.MypageDTO;
import com.ohgiraffers.ukki.user.model.dto.MypageInquiryDTO;
import com.ohgiraffers.ukki.user.model.dto.MypageReservationDTO;
import com.ohgiraffers.ukki.user.model.dto.MypageReviewDTO;
import com.ohgiraffers.ukki.user.model.service.CookieService;
import com.ohgiraffers.ukki.user.model.service.MypageService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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

    @PutMapping("/inquiry/{inquiryNo}")
    public ResponseEntity<Map<String, String>> updateInquiry(
            @PathVariable int inquiryNo,
            @RequestParam(required = false) MultipartFile file,
            @RequestParam String title,
            @RequestParam String text,
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

        if (file != null && !file.isEmpty()) {
            try {
                String filePath = mypageService.saveFile(file, userId);
                inquiryToUpdate.setFile(filePath);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Collections.singletonMap("message", "파일 업로드 실패: " + e.getMessage()));
            }
        }

        boolean updated = mypageService.updateInquiry(inquiryToUpdate, file, userId);

        if (updated) {
            return ResponseEntity.ok(Collections.singletonMap("message", "문의가 성공적으로 수정되었습니다."));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "문의 수정에 실패했습니다."));
        }
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

    // 스프링 프레임워크 리소스 사용 !
    @GetMapping("/file/download/{fileId}")
    public ResponseEntity<Resource> serveFile(@PathVariable String fileId, HttpServletRequest request) {
        String jwtToken = cookieService.getJWTCookie(request);

        if (jwtToken == null) {
            throw new IllegalArgumentException("토큰이 일치하지 않음");
        }

        String userId = jwtService.getUserInfoFromTokenId(jwtToken);
        if (userId == null) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        Resource resource = mypageService.loadFile(fileId, userId);

        if (resource == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}

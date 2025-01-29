package com.ohgiraffers.ukki.common.Controller;

import com.ohgiraffers.ukki.common.service.GoogleDriveService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoogleDriveController {

    private final GoogleDriveService googleDriveService;

    // GoogleDriveService 주입
    public GoogleDriveController(GoogleDriveService googleDriveService) {
        this.googleDriveService = googleDriveService;
    }

    @GetMapping("/image")
    public ResponseEntity<byte[]> getImage(@RequestParam String fileId) {
        if (fileId == null || fileId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("유효하지 않은 fileId입니다.".getBytes());
        }

        try {
            // GoogleDriveService를 통해 이미지 다운로드
            byte[] imageBytes = googleDriveService.downloadFile(fileId);

            // 이미지가 없거나 비어 있으면 오류 처리
            if (imageBytes == null || imageBytes.length == 0) {
                return handleErrorResponse("이미지 데이터가 없습니다.");
            }

            // 이미지 확장자 확인하여 Content-Type 설정
            String contentType = getImageContentType(imageBytes);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(imageBytes);

        } catch (Exception e) {
            e.printStackTrace();  // 예외 로그 출력
            return handleErrorResponse("이미지 다운로드 중 오류가 발생했습니다.");
        }
    }

    // 이미지를 가져와서 Content-Type을 설정
    private String getImageContentType(byte[] imageBytes) {
        if (imageBytes == null || imageBytes.length < 1) {
            return "octet-stream";  // 기본값
        }

        // PNG는 파일 시작 부분이 137 80 78 71 (0x89 0x50 0x4E 0x47) 이므로 이를 체크
        if (imageBytes[0] == (byte) 0x89 && imageBytes[1] == (byte) 0x50 && imageBytes[2] == (byte) 0x4E && imageBytes[3] == (byte) 0x47) {
            return "image/png";
        }
        // JPEG는 파일 시작 부분이 FF D8 FF (0xFF 0xD8 0xFF) 이므로 이를 체크
        else if (imageBytes[0] == (byte) 0xFF && imageBytes[1] == (byte) 0xD8 && imageBytes[2] == (byte) 0xFF) {
            return "image/jpeg";
        }
        // GIF는 파일 시작 부분이 47 49 46 (0x47 0x49 0x46) 이므로 이를 체크
        else if (imageBytes[0] == (byte) 0x47 && imageBytes[1] == (byte) 0x49 && imageBytes[2] == (byte) 0x46) {
            return "image/gif";
        }

        return "application/octet-stream";  // 기본값
    }

    // 오류 응답 처리 공통 메서드
    private ResponseEntity<byte[]> handleErrorResponse(String errorMessage) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorMessage.getBytes());
    }
}

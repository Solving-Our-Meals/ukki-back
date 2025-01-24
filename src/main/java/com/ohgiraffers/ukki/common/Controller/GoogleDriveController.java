package com.ohgiraffers.ukki.common.Controller;

import com.ohgiraffers.ukki.common.service.GoogleDriveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class GoogleDriveController {

    @Autowired
    private GoogleDriveService googleDriveService;

    @GetMapping("/image")
    public ResponseEntity<byte[]> getImage(@RequestParam String fileId) {
        try {
            if (fileId == null || fileId.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("유효하지 않은 fileId입니다.".getBytes());
            }

            String imageUrlWithId = "https://drive.google.com/uc?id=" + fileId;

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<byte[]> response = restTemplate.exchange(imageUrlWithId, HttpMethod.GET, null, byte[].class);

            HttpHeaders headers = new HttpHeaders();
            String contentType = response.getHeaders().getContentType() != null ?
                    response.getHeaders().getContentType().toString() :
                    "application/octet-stream";

            headers.set("Content-Type", contentType);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(response.getBody());  // 다운로드한 이미지를 응답 본문에 담아서 반환

        } catch (Exception e) {
            e.printStackTrace();  // 서버 로그에 오류 출력

            // 예외가 발생한 경우 500 Internal Server Error 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("이미지 다운로드 중 오류가 발생했습니다.".getBytes());
        }
    }
}

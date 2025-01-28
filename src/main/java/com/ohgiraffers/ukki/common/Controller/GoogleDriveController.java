package com.ohgiraffers.ukki.common.Controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

@RestController
public class GoogleDriveController {

    private static final String IMAGE_URL_TEMPLATE = "https://drive.google.com/uc?id=%s";

    @GetMapping("/image")
    public ResponseEntity<byte[]> getImage(@RequestParam String fileId) {
        if (fileId == null || fileId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("유효하지 않은 fileId입니다.".getBytes());
        }

        String imageUrlWithId = String.format(IMAGE_URL_TEMPLATE, fileId);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<byte[]> response = restTemplate.exchange(imageUrlWithId, HttpMethod.GET, null, byte[].class);

            // HTTP 상태 코드가 200 OK일 때만 진행
            if (response.getStatusCode() == HttpStatus.OK) {
                HttpHeaders headers = new HttpHeaders();
                String contentType = response.getHeaders().getContentType() != null ?
                        response.getHeaders().getContentType().toString() :
                        "application/octet-stream";
                headers.set("Content-Type", contentType);

                return ResponseEntity.ok()
                        .headers(headers)
                        .body(response.getBody());
            } else {
                return handleErrorResponse("이미지 요청이 실패했습니다. 상태 코드: " + response.getStatusCode());
            }

        } catch (HttpClientErrorException.NotFound e) {
            // 404 오류 발생 시
            return handleErrorResponse("이미지를 찾을 수 없습니다. (404 Not Found)");
        } catch (HttpClientErrorException.Forbidden e) {
            // 403 오류 발생 시
            return handleErrorResponse("이미지에 접근할 수 없습니다. (403 Forbidden)");
        } catch (Exception e) {
            // 기타 예외 처리
            e.printStackTrace();
            return handleErrorResponse("이미지 다운로드 중 오류가 발생했습니다.");
        }
    }

    // 오류 응답 처리 공통 메서드
    private ResponseEntity<byte[]> handleErrorResponse(String errorMessage) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorMessage.getBytes());
    }
}

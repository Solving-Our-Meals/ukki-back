package com.ohgiraffers.ukki.user.controller;

import com.ohgiraffers.ukki.store.model.dto.StoreInfoDTO;
import com.ohgiraffers.ukki.store.model.service.StoreService;
import com.ohgiraffers.ukki.user.model.dto.DirectionsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = {"/", "/main"})
public class MainController {

    private final StoreService storeService;
    private final RestTemplate restTemplate;

    @Autowired
    public MainController(StoreService storeService, RestTemplate restTemplate){
        this.storeService = storeService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/category")
    public List<StoreInfoDTO> getStoresLocation(@RequestParam("category") int category) {
        return storeService.getStoresLocation(category);
    }

    // 예시: Spring Boot를 사용하는 경우
    @RequestMapping(value = "/main/directions", method = RequestMethod.POST)
    public ResponseEntity<?> getDirections(@RequestBody DirectionsRequest request) {
        String responseBody = "";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 기본 상태는 INTERNAL_SERVER_ERROR

        try {
            String url = "https://apis-navi.kakaomobility.com/v1/waypoints/directions";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK 1d0bf0e6ccf05d20983add3f6153007d"); // 카카오 API 키

            HttpEntity<String> entity = new HttpEntity<>(request.toJson(), headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            // 성공적인 응답 처리
            if (response.getStatusCode() == HttpStatus.OK) {
                responseBody = response.getBody();  // 본문을 변수로 분리
                status = HttpStatus.OK;
            } else {
                responseBody = "경로를 찾을 수 없습니다.";  // 오류 메시지를 변수로 분리
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        } catch (Exception e) {
            responseBody = "오류 발생: " + e.getMessage();  // 예외 메시지를 변수로 분리
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        // 최종적으로 ResponseEntity를 반환
        return ResponseEntity.status(status).body(responseBody);
    }




}


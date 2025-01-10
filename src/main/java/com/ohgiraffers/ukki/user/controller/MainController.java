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
        try {
            String url = "https://apis-navi.kakaomobility.com/v1/waypoints/directions";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK 1d0bf0e6ccf05d20983add3f6153007d"); // 카카오 API 키

            HttpEntity<String> entity = new HttpEntity<>(request.toJson(), headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return ResponseEntity.ok(response.getBody());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("경로를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류 발생: " + e.getMessage());
        }
    }

}


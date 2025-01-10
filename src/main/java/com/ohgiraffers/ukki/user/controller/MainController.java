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

    @PostMapping("/directions")
    public ResponseEntity<String> getDirections(@RequestBody DirectionsRequest directionsRequest) {
        // 카카오 API 호출을 위한 URL
        String url = "https://apis-navi.kakaomobility.com/v1/waypoints/directions";

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK 1d0bf0e6ccf05d20983add3f6153007d");
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 바디 설정
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("origin", directionsRequest.getOrigin());
        requestBody.put("destination", directionsRequest.getDestination());
        requestBody.put("waypoints", directionsRequest.getWaypoints());

        // 요청 생성
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // RestTemplate을 사용하여 API 호출
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        return response;
    }
}


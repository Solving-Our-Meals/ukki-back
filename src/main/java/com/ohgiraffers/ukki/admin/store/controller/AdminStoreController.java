package com.ohgiraffers.ukki.admin.store.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohgiraffers.ukki.admin.store.model.dto.AdminStoreInfoDTO;
import com.ohgiraffers.ukki.admin.store.model.dto.AdminStoreListDTO;
import com.ohgiraffers.ukki.admin.store.model.dto.MonthlyRegistStoreDTO;
import com.ohgiraffers.ukki.admin.store.model.service.AdminStoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ohgiraffers.ukki.admin.store.model.dto.KeywordDTO;
import com.ohgiraffers.ukki.admin.store.model.dto.OperationDTO;

import java.util.*;

@RestController
@RequestMapping("/admin/stores")
public class AdminStoreController {

    private final AdminStoreService adminStoreService;

    public AdminStoreController(AdminStoreService adminStoreService){
        this.adminStoreService = adminStoreService;
    }

    @GetMapping("/monthly")
    public ResponseEntity<?> monthlyRegistStore(){
        try {
//            MonthlyRegistStoreDTO monthlyRegistStore = adminStoreService.monthlyRegistStore();

            List<MonthlyRegistStoreDTO> monthlyRegistStore = adminStoreService.monthlyRegistStore();

            System.out.println(monthlyRegistStore);
            return ResponseEntity.ok(monthlyRegistStore);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("월별 제휴가게 수 정보를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @GetMapping("/total")
    public ResponseEntity<?> totalRegistStore(){
        try {

            int total = adminStoreService.totalRegistStore();

            Map<String, Integer> response = new HashMap<>();

            response.put("totalStore", total);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("총 제휴가게 수 정보를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> searchAllStores(@RequestParam(required = false) String category, @RequestParam(required = false) String word){
        try {
            List<AdminStoreListDTO> storeList = adminStoreService.searchStores(category, word);

            return ResponseEntity.ok(storeList);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("가게리스트를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @GetMapping("/info/{storeNo}")
    public ResponseEntity<?> searchStoreInfo(@PathVariable int storeNo){
        try {
            System.out.println("여기 왔나");
            System.out.println(storeNo);

            AdminStoreInfoDTO storeInfo = adminStoreService.searchStoreInfo(storeNo);
            System.out.println(storeInfo);
            System.out.println("keyword 연결 전" + storeInfo);

            // keywordDB 연결
            KeywordDTO keywordDTO = adminStoreService.getKeyword(storeInfo.getStoreNo());
            storeInfo.setStoreKeyword(keywordDTO);

            // operationDB 연결
            OperationDTO operationDTO = adminStoreService.getOperation(storeInfo.getStoreNo());
            System.out.println(operationDTO);
            storeInfo.setOperationTime(operationDTO);



            System.out.println(keywordDTO);
            System.out.println("keyword연결 이후" + storeInfo);


            return ResponseEntity.ok(storeInfo);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("가게리스트를 불러오는 도중 에러가 발생했습니다.");
        }
    }

        @PutMapping("/info/{storeNo}/edit")
    public ResponseEntity<?> updateStore(
            @PathVariable Long storeNo,
            @RequestPart(value = "storeData", required = false) String storeDataJson,
            @RequestPart(value = "bannerStatus", required = false) String bannerStatus,
            @RequestPart(value = "menu", required = false) MultipartFile menuImage,
            @RequestPart(value = "profile", required = false) MultipartFile profileImage,
            @RequestPart(value = "banner1", required = false) MultipartFile banner1,
            @RequestPart(value = "banner2", required = false) MultipartFile banner2,
            @RequestPart(value = "banner3", required = false) MultipartFile banner3,
            @RequestPart(value = "banner4", required = false) MultipartFile banner4,
            @RequestPart(value = "banner5", required = false) MultipartFile banner5
    ) {
        try {
            // JSON 문자열을 객체로 변환
            ObjectMapper mapper = new ObjectMapper();
            AdminStoreInfoDTO storeData = null;
            if (storeDataJson != null) {
                storeData = mapper.readValue(storeDataJson, AdminStoreInfoDTO.class);
            }

            System.out.println("배너상태 파싱한거 : "+bannerStatus);
            String[] bannerStatusParse = null;
            if (bannerStatus != null) {
                bannerStatusParse = mapper.readValue(bannerStatus, String[].class);
            }

            // 배너 이미지 리스트 생성
            Map<Integer, MultipartFile> bannerUpdates = new HashMap<>();
            if (banner1 != null) bannerUpdates.put(1, banner1);
            if (banner2 != null) bannerUpdates.put(2, banner2);
            if (banner3 != null) bannerUpdates.put(3, banner3);
            if (banner4 != null) bannerUpdates.put(4, banner4);
            if (banner5 != null) bannerUpdates.put(5, banner5);

//            bannerNum = bannerUpdates.keySet();
            System.out.println(bannerUpdates.keySet());

//            현재 bannerUpdates에는 업데이트된 배너에 대한 정보만 가지고 있다.
//            파일이름 = 가게번호+"banner"+키+"png" 파일은 value
//            키 값을 bannerUpdates.keySet()으로 Set에 담고 Collection.max()를 실행시켜서 max값 < 5일 경우 사이에 있는 파일을 지우는 for문 실행시키자.

//            bannerStatusParse에서 빈배열은 null으로 BannerDTO에 담아서 업데이트.
//            menu와 profile은 null이 아니면 업데이트 실행

//            data에는 keyword와 opration이 배열로 담겨있다. 이걸 뽑아내고 각자 구역에 update
//            data에 category가 스트링으로 넘어온다. 이걸 해결하려면 그냥 info가 no가 되고 category table내용도 같이 넘겨버려야할 것 같다.

            System.out.println(bannerUpdates);

            // 서비스 호출
            // adminStoreService.updateStore(storeNo, storeData, menuImage, profileImage, bannerImages);

            return ResponseEntity.ok().body("가게 정보가 성공적으로 수정되었습니다.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("가게 정보 수정 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}

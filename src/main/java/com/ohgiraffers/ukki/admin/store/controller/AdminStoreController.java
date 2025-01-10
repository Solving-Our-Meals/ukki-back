package com.ohgiraffers.ukki.admin.store.controller;

import com.ohgiraffers.ukki.admin.store.model.dto.AdminStoreInfoDTO;
import com.ohgiraffers.ukki.admin.store.model.dto.AdminStoreListDTO;
import com.ohgiraffers.ukki.admin.store.model.dto.MonthlyRegistStoreDTO;
import com.ohgiraffers.ukki.admin.store.model.service.AdminStoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ohgiraffers.ukki.admin.store.model.dto.KeywordDTO;
import com.ohgiraffers.ukki.admin.store.model.dto.OperationDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}

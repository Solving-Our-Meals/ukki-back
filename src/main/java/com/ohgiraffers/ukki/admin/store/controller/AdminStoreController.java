package com.ohgiraffers.ukki.admin.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohgiraffers.ukki.admin.store.model.dto.*;
import com.ohgiraffers.ukki.admin.store.model.service.AdminStoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@RestController
@RequestMapping("/admin/stores")
public class AdminStoreController {

    private final String SHARED_FOLDER = "\\\\DESKTOP-KLQ0O04\\Users\\admin\\Desktop\\ukkiImg";
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

            AdminStoreInfoDTO storeInfo = adminStoreService.searchStoreInfo(storeNo);
            System.out.println(storeInfo);

            // keywordDB 연결
            KeywordDTO keywordDTO = adminStoreService.getKeyword(storeInfo.getStoreNo());
            storeInfo.setStoreKeyword(keywordDTO);

            // operationDB 연결
            OperationDTO operationDTO = adminStoreService.getOperation(storeInfo.getStoreNo());
            storeInfo.setOperationTime(operationDTO);

            List<CategoryDTO> categoryDTO = adminStoreService.getCategory();
            storeInfo.setStoreCategory(categoryDTO);


            return ResponseEntity.ok(storeInfo);
        } catch (Exception e) {
            // 에러 메시지 로그 출력
            e.printStackTrace();
            // 적절한 에러 메시지와 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("가게리스트를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @DeleteMapping("/info/{storeNo}/delete")
    @Transactional
    public ResponseEntity<?> deleteStoreInfo(@PathVariable int storeNo){
        System.out.println("삭제하러 옴");
        Map<String, String> response = new HashMap<>();
        String message = "";
        try {

            int result = adminStoreService.deleteStoreInfo(storeNo);

            if(result > 1){
                message = "삭제에 실패했습니다.";
            }else{
                message = "삭제에 성공했습니다.";
            }

            response.put("message", message);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            message = "가게 정보를 삭제하는 도중 에러가 발생했습니다.";
            response.put("message", message);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

        @PutMapping("/info/{storeNo}/edit")
        @Transactional
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
            System.out.println(profileImage);
            System.out.println(menuImage);

            // JSON 문자열을 객체로 변환
            ObjectMapper mapper = new ObjectMapper();
            AdminStoreInfoDTO storeData = new AdminStoreInfoDTO();
            if (storeDataJson != null) {
                storeData = mapper.readValue(storeDataJson, AdminStoreInfoDTO.class);
                if(storeData.getOperationTime() != null){
                    adminStoreService.editOperationTime(storeData.getOperationTime());
                }
                if(storeData.getStoreKeyword() != null){
                    adminStoreService.editKeyword(storeData.getStoreKeyword());
                }
            }
            
            String[] bannerStatusParse = null;

            int count = 0;
            if (bannerStatus != null) {
                bannerStatusParse = mapper.readValue(bannerStatus, String[].class);
                count = (int) Arrays.stream(bannerStatusParse)
                       .filter(banner -> banner.contains("/store/api/files?filename="))
                       .count();
            }
            System.out.println("포함된 갯수: " + count);

            
            if(menuImage != null){
                String fileName = storeNo+"menu";
                System.out.println("메뉴 들어옴");
                System.out.println(menuImage);
                int result = fileController(menuImage, fileName);
                if(result == 2){
                    System.out.println("메뉴성공");
                    storeData.setStoreMenu(fileName);
                }else{
                    System.out.println("메뉴실패");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("메뉴 이미지 업데이트 중 오류가 발생했습니다.");
                }
            }
            if(profileImage != null){
                String fileName = storeNo+"profile";
                System.out.println("프로필 파일 보낸다" + fileName);
                int result = fileController(profileImage, fileName);
                if(result == 2){
                    System.out.println("프로필 성공 보낸다" + fileName);
                    storeData.setStoreProfile(fileName);
                }else{
                    System.out.println("실패");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("프로필 이미지 업데이트 중 오류가 발생했습니다.");
                }
            }

            // 배너 이미지 리스트 생성
            Map<Integer, MultipartFile> bannerUpdates = new HashMap<>();
            if (banner1 != null) bannerUpdates.put(1, banner1);
            if (banner2 != null) bannerUpdates.put(2, banner2);
            if (banner3 != null) bannerUpdates.put(3, banner3);
            if (banner4 != null) bannerUpdates.put(4, banner4);
            if (banner5 != null) bannerUpdates.put(5, banner5);

//            현재 bannerUpdates에는 업데이트된 배너에 대한 정보만 가지고 있다.
//            파일이름 = 가게번호+"banner"+키+"png" 파일은 value
//            키 값을 bannerUpdates.keySet()으로 Set에 담고 Collection.max()를 실행시켜서 max값 < 5일 경우 사이에 있는 파일을 지우는 for문 실행시키자.

//            bannerStatusParse에서 빈배열은 null으로 BannerDTO에 담아서 업데이트.
//            menu와 profile은 null이 아니면 업데이트 실행

//            data에는 keyword와 opration이 배열로 담겨있다. 이걸 뽑아내고 각자 구역에 update
//            data에 category가 스트링으로 넘어온다. 이걸 해결하려면 그냥 info가 no가 되고 category table내용도 같이 넘겨버려야할 것 같다.
//
            System.out.println(!bannerUpdates.isEmpty());
            System.out.println(bannerUpdates.size());
            int maxBannerKey = 0;
            if (!bannerUpdates.isEmpty()) { // bannerUpdates가 존재할 때만 진행
                maxBannerKey = count+bannerUpdates.size();
            }else{
                maxBannerKey = count;
            }
            if (maxBannerKey < 5) {
                System.out.println(maxBannerKey);
                for (int i = maxBannerKey+1; i <= 5; i++) {
                    String fileToDelete = storeNo + "banner" + i + ".png";
                    Path filePath = Paths.get(SHARED_FOLDER, fileToDelete);
                    Files.deleteIfExists(filePath);
                }
            }

            for (Map.Entry<Integer, MultipartFile> entry : bannerUpdates.entrySet()) {
                int key = entry.getKey();
                MultipartFile file = entry.getValue();

                // 파일 이름 생성
                String fileName = storeNo + "banner" + key;

                // fileController 호출하여 파일 저장
                int result = fileController(file, fileName);
                if (result == 2) {
                    System.out.println("배너 파일 저장 성공: " + fileName);
                } else {
                    System.out.println("배너 파일 저장 실패: " + fileName);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("배너 이미지 업데이트 중 오류가 발생했습니다.");
                }
            }


            // 서비스 호출
            // adminStoreService.updateStore(storeNo, storeData, menuImage, profileImage, bannerImages);
            
            // 모든 작업이 완료된 후 storeData를 업데이트
            if (storeData != null) {
                adminStoreService.editStore(storeData);
            }

            return ResponseEntity.ok().body("가게 정보가 성공적으로 수정되었습니다.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("가게 정보 수정 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public int fileController(MultipartFile file, String fileName){

        System.out.println("fileController 넘어옴" + fileName);
        String fileSetName = fileName+".png";
        try{
//                경로설정
            Path networkPath = Paths.get(SHARED_FOLDER);
            if(!Files.exists(networkPath)){
                Files.createDirectories(networkPath);
            }

//                파일 저장 - 경로에 파일이름 붙이기
//                StandardCopyOption.REPLACE_EXISTING은 대상 경로에 동일한 이름의 파일이 이미 존재할 경우 그 카일을 덮어쓰도록하는 옵션
            Path filePath = networkPath.resolve(fileSetName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            System.out.println(file.getInputStream());
            System.out.println("파일 저장 성공: " + fileSetName);
            return 2;
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
    }
}

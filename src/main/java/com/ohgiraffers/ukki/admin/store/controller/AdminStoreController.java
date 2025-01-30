package com.ohgiraffers.ukki.admin.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohgiraffers.ukki.admin.store.model.dto.*;
import com.ohgiraffers.ukki.admin.store.model.service.AdminStoreService;
import com.ohgiraffers.ukki.common.service.GoogleDriveService;
import com.ohgiraffers.ukki.store.model.dto.StoreInfoDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import org.springframework.http.MediaType;

@RestController
@RequestMapping("/admin/stores")
public class AdminStoreController {

//    private final String SHARED_FOLDER = "\\\\DESKTOP-KLQ0O04\\Users\\admin\\Desktop\\ukkiImg";
//private final String SHARED_FOLDER = "\\\\I7E-74\\ukki_nas\\store";
    private final AdminStoreService adminStoreService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final GoogleDriveService googleDriveService;
    @Value("${google.drive.inquiry-folder-id}")
    private String STORE_FOLDER_ID;
    
    @Autowired
    public AdminStoreController(AdminStoreService adminStoreService, BCryptPasswordEncoder passwordEncoder, GoogleDriveService googleDriveService) {
        this.adminStoreService = adminStoreService;
        this.passwordEncoder = passwordEncoder;
        this.googleDriveService = googleDriveService;
    }

    @GetMapping("/monthly")
    public ResponseEntity<?> monthlyRegistStore(){
        try {
            List<MonthlyRegistStoreDTO> monthlyRegistStore = adminStoreService.monthlyRegistStore();

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(monthlyRegistStore);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body("월별 제휴가게 수 정보를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @GetMapping("/total")
    public ResponseEntity<?> totalRegistStore(){
        try {

            int total = adminStoreService.totalRegistStore();

            Map<String, Integer> response = new HashMap<>();

            response.put("totalStore", total);

            return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body("총 제휴가게 수 정보를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> searchStores(
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String word) {
        
        try {
            List<AdminStoreListDTO> stores = adminStoreService.searchStores(category, word);
            
            // 검색 결과가 없는 경우
            if (stores.isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "검색 결과가 없습니다.");
                return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
            }

            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(stores);
            
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "검색 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponse);
        }
    }

    @GetMapping("/info/{storeNo}")
    public ResponseEntity<?> searchStoreInfo(@PathVariable int storeNo){
        try {

            AdminStoreInfoDTO storeInfo = adminStoreService.searchStoreInfo(storeNo);

            // keywordDB 연결
            AdminStoreKeywordDTO adminStoreKeywordDTO = adminStoreService.getKeyword(storeInfo.getStoreNo());
            storeInfo.setStoreKeyword(adminStoreKeywordDTO);

            // operationDB 연결
            AdminStoreOperationDTO adminStoreOperationDTO = adminStoreService.getOperation(storeInfo.getStoreNo());
            storeInfo.setOperationTime(adminStoreOperationDTO);

            List<AdminStoreCategoryDTO> adminStoreCategoryDTO = adminStoreService.getCategory();
            storeInfo.setStoreCategory(adminStoreCategoryDTO);

            return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(storeInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body("가게리스트를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @DeleteMapping("/info/{storeNo}/delete")
    public ResponseEntity<?> deleteStoreInfo(@PathVariable int storeNo){
        Map<String, String> response = new HashMap<>();
        String message = "";
        try {
            long storeNoLong = (long) storeNo;
            AdminStoreInfoDTO storeData = adminStoreService.searchStoreInfo(storeNoLong);
            String menu = storeData.getStoreMenu();
            String profile = storeData.getStoreProfile();
            AdminStoreBannerDTO bannerData = adminStoreService.getBanner(storeNoLong);
            String[] banner = bannerData.getBannerList().toArray(new String[0]);

            int result = adminStoreService.deleteStoreInfo(storeNo);

            if(result > 0){
                message = "삭제에 성공했습니다.";
                adminStoreService.deleteStoreBanner(storeNo);
                for (int i = 1; i <= 5; i++) {
//                    String fileToDelete = storeNo + "banner" + i + ".png";
//                    Path filePath = Paths.get(SHARED_FOLDER, fileToDelete);
//                    Files.deleteIfExists(filePath);
                }
//                String fileToDeleteProfile = storeNo + "profile" + ".png";
//                Path filePathProfile = Paths.get(SHARED_FOLDER, fileToDeleteProfile);
//                Files.deleteIfExists(filePathProfile);
//                String fileToDeleteMenu = storeNo + "menu" + ".png";
//                Path filePathMenu = Paths.get(SHARED_FOLDER, fileToDeleteMenu);
//                Files.deleteIfExists(filePathMenu);
//                adminStoreService.deleteStoreKeyword(storeNo);
//                adminStoreService.deleteStoreOperation(storeNo);
//                deleteReviewWithStore(storeNo);
            }else{
                message = "삭제에 실패했습니다.";
            }

            response.put("message", message);

            return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(response);
        } catch (Exception e) {
            e.printStackTrace();
            message = "가게 정보를 삭제하는 도중 에러가 발생했습니다.";
            response.put("message", message);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body(response);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteReviewWithStore(int storeNo) throws IOException {
        String[] reviewImgArray = adminStoreService.getReviewImgStoreNo(storeNo);
        for(int i = 0; i < reviewImgArray.length; i++){
            String reviewImg = reviewImgArray[i]+".png";
//            Path filePathProfile = Paths.get(SHARED_FOLDER, reviewImg);
//            Files.deleteIfExists(filePathProfile);
        }
        adminStoreService.deleteReviewWithStore(storeNo);

    }

        @PutMapping("/info/{storeNo}/edit")
        @Transactional(rollbackFor = Exception.class)
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

            AdminStoreInfoDTO storeInfo = adminStoreService.searchStoreInfo(storeNo);
            AdminStoreBannerDTO bannerInfo = adminStoreService.getBanner(storeNo);
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
            if (bannerStatus != null) {
                bannerStatusParse = mapper.readValue(bannerStatus, String[].class);
            }

            
            if(menuImage != null){
                googleDriveService.deleteFile(storeInfo.getStoreMenu());
                String fileName = storeNo+"menu";
                String menuResult = googleDriveService.uploadFile(menuImage, STORE_FOLDER_ID, fileName);
                if(menuResult != null){
                    storeData.setStoreMenu(menuResult);
                }else{
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("메뉴 이미지 업데이트 중 오류가 발생했습니다.");
                }
            }
            if(profileImage != null){
                googleDriveService.deleteFile(storeInfo.getStoreProfile());
                String fileName = storeNo+"profile";
                String profileResult = googleDriveService.uploadFile(profileImage, STORE_FOLDER_ID, fileName);
                if(profileResult != null){
                    storeData.setStoreProfile(profileResult);
                }else{
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("프로필 이미지 업데이트 중 오류가 발생했습니다.");
                }
            }

            // 배너 이미지 처리
            Map<Integer, MultipartFile> bannerUpdates = new HashMap<>();
            if (banner1 != null) bannerUpdates.put(1, banner1);
            if (banner2 != null) bannerUpdates.put(2, banner2);
            if (banner3 != null) bannerUpdates.put(3, banner3);
            if (banner4 != null) bannerUpdates.put(4, banner4);
            if (banner5 != null) bannerUpdates.put(5, banner5);

            String[] bannerNameArr = new String[5];

            // 기존 배너 정보 유지 (클라이언트가 삭제하지 않은 배너)
            if (bannerStatusParse != null) {
                bannerNameArr[0] = bannerStatusParse[0] != null ? bannerInfo.getBanner1() : null;
                bannerNameArr[1] = bannerStatusParse[1] != null ? bannerInfo.getBanner2() : null;
                bannerNameArr[2] = bannerStatusParse[2] != null ? bannerInfo.getBanner3() : null;
                bannerNameArr[3] = bannerStatusParse[3] != null ? bannerInfo.getBanner4() : null;
                bannerNameArr[4] = bannerStatusParse[4] != null ? bannerInfo.getBanner5() : null;
            }

            // 새로운 배너 이미지 업로드
            for (Map.Entry<Integer, MultipartFile> entry : bannerUpdates.entrySet()) {
                int key = entry.getKey();
                MultipartFile file = entry.getValue();
                
                // 기존 배너 삭제 로직
                String existingBannerId = null;
                switch(key) {
                    case 1: existingBannerId = bannerInfo.getBanner1(); break;
                    case 2: existingBannerId = bannerInfo.getBanner2(); break;
                    case 3: existingBannerId = bannerInfo.getBanner3(); break;
                    case 4: existingBannerId = bannerInfo.getBanner4(); break;
                    case 5: existingBannerId = bannerInfo.getBanner5(); break;
                }

                // 기존 배너가 있으면 삭제
                if (existingBannerId != null && !existingBannerId.isEmpty()) {
                    googleDriveService.deleteFile(existingBannerId);
                }

                // 새 이미지 업로드 - fileId 직접 저장
                String fileName = storeNo + "banner" + key;
                String fileId = googleDriveService.uploadFile(file, STORE_FOLDER_ID, fileName);
                bannerNameArr[key-1] = fileId;  // URL이 아닌 fileId 저장
            }

            // 배너 정보 업데이트
            AdminStoreBannerDTO adminStoreBannerDTO = new AdminStoreBannerDTO(storeNo, bannerNameArr[0], bannerNameArr[1],
                bannerNameArr[2], bannerNameArr[3], bannerNameArr[4]);
            System.out.println("bannerDTO : "+ adminStoreBannerDTO);
            adminStoreService.editBanner(adminStoreBannerDTO);

            if (storeData != null) {
                adminStoreService.editStore(storeData);
            }

            return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body("가게 정보가 성공적으로 수정되었습니다.");

        } catch (Exception e) {
                e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body("가게 정보 수정 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @PostMapping("/regist/user")
    public ResponseEntity<?> registerUser(@RequestBody AdminStoreUserDTO userDTO, HttpSession session) {
        // 세션에 저장해서 보내기
//        form을 이용해 클라이언트 쪽에서 직접 보내는 것은 보안성과 코드 일관성에 좋지 않은 영향을 줄 수 있기때문에 세션방식 채택
        System.out.println(userDTO);
        System.out.println(userDTO.getUserId());
        session.setAttribute("userId", userDTO.getUserId());
        session.setAttribute("userPassword", userDTO.getUserPassword());
        session.setAttribute("userName", userDTO.getUserName());
        session.setAttribute("email", userDTO.getEmail());
        System.out.println(session.getAttribute("userId"));
        System.out.println(session.getAttribute("userPassword"));
        System.out.println(session.getAttribute("userName"));
        System.out.println(session.getAttribute("email"));
        System.out.println(session.getId());
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered and session created successfully");
            return ResponseEntity.ok()
            .body(response);
    }

    @GetMapping("regist/store")
    public ResponseEntity<?> ShowStoreInfoRegistPage(HttpSession session) {

        System.out.println(session.getId());
        System.out.println(session.getAttribute("userId"));
        System.out.println(session.getAttribute("userPassword"));
        System.out.println(session.getAttribute("userName"));
        System.out.println(session.getAttribute("email"));
        List<AdminStoreCategoryDTO> adminStoreCategoryDTO = adminStoreService.getCategory();
        AdminStoreUserDTO userDTO = new AdminStoreUserDTO();
        userDTO.setUserId(session.getAttribute("userId").toString());
        userDTO.setUserPassword(session.getAttribute("userPassword").toString());
        userDTO.setUserName(session.getAttribute("userName").toString());
        userDTO.setEmail(session.getAttribute("email").toString());

        Map<String, Object> response = new HashMap<>();
        response.put("categoryDTO", adminStoreCategoryDTO);
        response.put("userDTO", userDTO);


        return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(response);
    }

    @PostMapping("cancel/user")
    public ResponseEntity<?> cancelUserSession(HttpSession session) {
        session.removeAttribute("userId");
        session.removeAttribute("userPassword");
        session.removeAttribute("userName");
        session.removeAttribute("email");
        Map<String, Object> response = new HashMap<>();
        response.put("userDTO", new AdminStoreUserDTO());
        return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(response);
    }

    @PostMapping("regist/store")
    public ResponseEntity<?> registStore(
            @RequestPart(value = "storeData", required = false) String storeDataJson,
            @RequestPart(value = "userData", required = false) String userDataJson,
            @RequestPart(value = "menu", required = false) MultipartFile menuImage,
            @RequestPart(value = "profile", required = false) MultipartFile profileImage,
            @RequestPart(value = "banner1", required = false) MultipartFile banner1,
            @RequestPart(value = "banner2", required = false) MultipartFile banner2,
            @RequestPart(value = "banner3", required = false) MultipartFile banner3,
            @RequestPart(value = "banner4", required = false) MultipartFile banner4,
            @RequestPart(value = "banner5", required = false) MultipartFile banner5
    ){
        try {
            int storeNo = adminStoreService.lastStoreNo()+1;
            // JSON 문자열을 객체로 변환
            ObjectMapper mapper = new ObjectMapper();
            AdminStoreInfoDTO storeData = new AdminStoreInfoDTO();
            storeData = mapper.readValue(storeDataJson, AdminStoreInfoDTO.class);

            AdminStoreOperationDTO adminStoreOperationDTO = storeData.getOperationTime();
            adminStoreOperationDTO.setStoreNo(storeNo);

            String menuName = storeNo+"menu";

            String menuResult = googleDriveService.uploadFile(menuImage, STORE_FOLDER_ID, menuName);
            storeData.setStoreMenu(menuResult);

            String fileName = storeNo+"profile";
            String profileResult = googleDriveService.uploadFile(profileImage, STORE_FOLDER_ID, fileName);
            storeData.setStoreProfile(profileResult);

            // 배너 이미지 리스트 생성
            Map<Integer, MultipartFile> bannerUpdates = new HashMap<>();
            if (banner1 != null) bannerUpdates.put(1, banner1);
            if (banner2 != null) bannerUpdates.put(2, banner2);
            if (banner3 != null) bannerUpdates.put(3, banner3);
            if (banner4 != null) bannerUpdates.put(4, banner4);
            if (banner5 != null) bannerUpdates.put(5, banner5);

            String[] bannerNameArr = new String[5];

            for(Map.Entry<Integer, MultipartFile> entry : bannerUpdates.entrySet()) {
                int key = entry.getKey();
                MultipartFile file = entry.getValue();
                String bannerName = storeNo + "banner" + key;

                String result = googleDriveService.uploadFile(file, STORE_FOLDER_ID ,bannerName);
                bannerNameArr[key-1] = result;

                // fileController 호출하여 파일 저장


            }
            AdminStoreBannerDTO adminStoreBannerDTO = new AdminStoreBannerDTO(storeNo, bannerNameArr[0], bannerNameArr[1], bannerNameArr[2], bannerNameArr[3], bannerNameArr[4]);

            int bannerResult = adminStoreService.insertBanner(adminStoreBannerDTO);
            if(bannerResult > 0){
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("배너 업데이트 중 오류가 발생했습니다.");
            }

            AdminStoreUserDTO userData = new AdminStoreUserDTO();
            userData = mapper.readValue(userDataJson, AdminStoreUserDTO.class);
            String encodePwd = passwordEncoder.encode(userData.getUserPassword());
            userData.setUserPassword(encodePwd);

            adminStoreService.insertStoreUser(userData);
            int userNo = adminStoreService.searchCurrentStoreUser(userData.getUserId());

            storeData.setStoreNo(storeNo);

            storeData.setUserNo(userNo);
            int operationResult = adminStoreService.insertOperationTime(adminStoreOperationDTO);
            if(operationResult > 0){
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("운영시간 업데이트 중 오류가 발생했습니다.");
            }

            AdminStoreKeywordDTO adminStoreKeywordDTO = storeData.getStoreKeyword();
            adminStoreKeywordDTO.setStoreNo(storeNo);
            int keywordResult = adminStoreService.insertKeyword(adminStoreKeywordDTO);
            if(keywordResult > 0){
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("키워드 업데이트 중 오류가 발생했습니다.");
            }
            adminStoreService.insertStore(storeData);


            return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body("가게 정보가 성공적으로 등록되었습니다.");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body("가게 정보 등록 중 오류가 발생했습니다: " + e.getMessage());
        }

    }

}

package com.ohgiraffers.ukki.admin.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohgiraffers.ukki.admin.store.model.dto.*;
import com.ohgiraffers.ukki.admin.store.model.service.AdminStoreService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
private final String SHARED_FOLDER = "\\\\I7E-74\\ukki_nas\\store";
    private final AdminStoreService adminStoreService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AdminStoreController(AdminStoreService adminStoreService, BCryptPasswordEncoder passwordEncoder) {
        this.adminStoreService = adminStoreService;
        this.passwordEncoder = passwordEncoder;
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
    public ResponseEntity<?> searchAllStores(@RequestParam(required = false) String category, @RequestParam(required = false) String word){
        try {
            List<AdminStoreListDTO> storeList = adminStoreService.searchStores(category, word);

            return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(storeList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body("가게리스트를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @GetMapping("/info/{storeNo}")
    public ResponseEntity<?> searchStoreInfo(@PathVariable int storeNo){
        try {

            AdminStoreInfoDTO storeInfo = adminStoreService.searchStoreInfo(storeNo);

            // keywordDB 연결
            KeywordDTO keywordDTO = adminStoreService.getKeyword(storeInfo.getStoreNo());
            storeInfo.setStoreKeyword(keywordDTO);

            // operationDB 연결
            OperationDTO operationDTO = adminStoreService.getOperation(storeInfo.getStoreNo());
            storeInfo.setOperationTime(operationDTO);

            List<CategoryDTO> categoryDTO = adminStoreService.getCategory();
            storeInfo.setStoreCategory(categoryDTO);

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

            int result = adminStoreService.deleteStoreInfo(storeNo);

            if(result > 0){
                message = "삭제에 성공했습니다.";
                adminStoreService.deleteStoreBanner(storeNo);
                for (int i = 1; i <= 5; i++) {
                    String fileToDelete = storeNo + "banner" + i + ".png";
                    Path filePath = Paths.get(SHARED_FOLDER, fileToDelete);
                    Files.deleteIfExists(filePath);
                }
                String fileToDeleteProfile = storeNo + "profile" + ".png";
                Path filePathProfile = Paths.get(SHARED_FOLDER, fileToDeleteProfile);
                Files.deleteIfExists(filePathProfile);
                String fileToDeleteMenu = storeNo + "menu" + ".png";
                Path filePathMenu = Paths.get(SHARED_FOLDER, fileToDeleteMenu);
                Files.deleteIfExists(filePathMenu);
                adminStoreService.deleteStoreKeyword(storeNo);
                adminStoreService.deleteStoreOperation(storeNo);
                deleteReviewWithStore(storeNo);
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
            Path filePathProfile = Paths.get(SHARED_FOLDER, reviewImg);
            Files.deleteIfExists(filePathProfile);
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
                       .filter(banner -> banner.contains("/store/" + storeNo + "/api/files?filename="))
                       .count();
            }

            
            if(menuImage != null){
                String fileName = storeNo+"menu";
                int result = fileController(menuImage, fileName);
                if(result == 2){
                    storeData.setStoreMenu(fileName);
                }else{
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("메뉴 이미지 업데이트 중 오류가 발생했습니다.");
                }
            }
            if(profileImage != null){
                String fileName = storeNo+"profile";
                int result = fileController(profileImage, fileName);
                if(result == 2){
                    storeData.setStoreProfile(fileName);
                }else{
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


            int maxBannerKey = 0;
            if (!bannerUpdates.isEmpty()) { // bannerUpdates가 존재할 때만 진행
                maxBannerKey = count+bannerUpdates.size();
            }else{
                maxBannerKey = count;
            }
            if (maxBannerKey < 5) {
                for (int i = maxBannerKey+1; i <= 5; i++) {
                    String fileToDelete = storeNo + "banner" + i + ".png";
                    Path filePath = Paths.get(SHARED_FOLDER, fileToDelete);
                    Files.deleteIfExists(filePath);
                }
            }

            String[] bannerNameArr = new String[5];
            for(int i = 0; i < count; i++){
                bannerNameArr[i] = storeNo+"banner"+(i+1);
            }
            for (Map.Entry<Integer, MultipartFile> entry : bannerUpdates.entrySet()) {
                int key = entry.getKey();
                MultipartFile file = entry.getValue();

                // 파일 이름 생성
                String fileName = storeNo + "banner" + key;
                bannerNameArr[key-1] = fileName;
                // fileController 호출하여 파일 저장
                int result = fileController(file, fileName);
                if (result == 2) {

                } else {

                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("배너 이미지 업데이트 중 오류가 발생했습니다.");
                }
            }

            BannerDTO bannerDTO = new BannerDTO(storeNo, bannerNameArr[0], bannerNameArr[1], bannerNameArr[2], bannerNameArr[3], bannerNameArr[4]);
            adminStoreService.editBanner(bannerDTO);

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
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered and session created successfully");

            return ResponseEntity.ok()
            .body(response);
    }

    @GetMapping("regist/store")
    public ResponseEntity<?> ShowStoreInfoRegistPage(HttpSession session) {

        System.out.println(session.getAttribute("userId"));
        System.out.println(session.getAttribute("userPassword"));
        System.out.println(session.getAttribute("userName"));
        System.out.println(session.getAttribute("email"));
        List<CategoryDTO> categoryDTO = adminStoreService.getCategory();
        AdminStoreUserDTO userDTO = new AdminStoreUserDTO();
        userDTO.setUserId(session.getAttribute("userId").toString());
        userDTO.setUserPassword(session.getAttribute("userPassword").toString());
        userDTO.setUserName(session.getAttribute("userName").toString());
        userDTO.setEmail(session.getAttribute("email").toString());

        Map<String, Object> response = new HashMap<>();
        response.put("categoryDTO", categoryDTO);
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

            OperationDTO operationDTO = storeData.getOperationTime();
            operationDTO.setStoreNo(storeNo);
            int operationResult = adminStoreService.insertOperationTime(operationDTO);
            if(operationResult > 0){

            }else{

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("운영시간 업데이트 중 오류가 발생했습니다.");
            }

            KeywordDTO keywordDTO = storeData.getStoreKeyword();
            keywordDTO.setStoreNo(storeNo);
            int keywordResult = adminStoreService.insertKeyword(keywordDTO);
            if(keywordResult > 0){

            }else{

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("키워드 업데이트 중 오류가 발생했습니다.");
            }

            String menuName = storeNo+"menu";

            int menuResult = fileController(menuImage, menuName);
            if(menuResult == 2){

                storeData.setStoreMenu(menuName);
            }else{

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("메뉴 이미지 업데이트 중 오류가 발생했습니다.");
            }

            String fileName = storeNo+"profile";
            int profileResult = fileController(profileImage, fileName);
            if(profileResult == 2){
                storeData.setStoreProfile(fileName);
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("프로필 이미지 업데이트 중 오류가 발생했습니다.");
            }


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

                bannerNameArr[key-1] = bannerName;

                // fileController 호출하여 파일 저장
                int result = fileController(file, bannerName);
                if (result == 2) {
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("배너 이미지 업데이트 중 오류가 발생했습니다.");
                }
            }
            BannerDTO bannerDTO = new BannerDTO(storeNo, bannerNameArr[0], bannerNameArr[1], bannerNameArr[2], bannerNameArr[3], bannerNameArr[4]);

            int bannerResult = adminStoreService.insertBanner(bannerDTO);
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

    public int fileController(MultipartFile file, String fileName){

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
            return 2;
        } catch (IOException e) {
            e.printStackTrace();
            return 1;   
        }
    }
}

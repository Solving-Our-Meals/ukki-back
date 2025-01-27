package com.ohgiraffers.ukki.store.controller;

import com.ohgiraffers.ukki.common.service.GoogleDriveService;
import com.ohgiraffers.ukki.store.model.dto.*;
import com.ohgiraffers.ukki.store.model.service.BossService;
import com.ohgiraffers.ukki.store.model.service.StoreService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/store")
@CrossOrigin(origins = "http://localhost:3000")
public class StoreController {

    private final StoreService storeService;
//    private final String SHARED_FOLDER = "\\\\I7E-74\\ukki_nas\\store";
    private final String SHARED_FOLDER = "\\\\Desktop-43runa1\\images\\store";
    private final GoogleDriveService googleDriveService;

    private static final String INQUIRY_FOLDER_ID = "1Bzigy3LlWfu5wAj7vB5Xdp_QapW76eQG";
    private static final String STORE_FOLDER_ID = "19mb-5n8hNjrdRAksh4hTKcFY-Gp0Aaoz";
    private static final String REVIEW_FOLDER_ID = "1wJz5yiUrtwNiK3tk1rN1vGSrly8NiIQr";

    public StoreController(StoreService storeService, BossService bossService, GoogleDriveService googleDriveService){
        this.storeService = storeService;
        this.googleDriveService = googleDriveService;
    }

    // 검색 페이지 만들어지면 pathvariable로 변경하기
    @GetMapping(value="/{storeNo}/getInfo", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public ResponseEntity<?> getStoreInfo(@PathVariable("storeNo") long storeNo, @ModelAttribute StoreInfoDTO storeInfoDTO){
        try{
            // storeDB 연결
            storeInfoDTO = storeService.getStoreInfo(storeNo);

            // keywordDB 연결
            KeywordDTO keywordDTO = storeService.getKeyword(storeNo);
            storeInfoDTO.setStoreKeyword(keywordDTO);

            // operationDB 연결
            OperationDTO operationDTO = storeService.getOperation(storeNo);
            storeInfoDTO.setOperationTime(operationDTO);

            System.out.println("가게 정보" + storeInfoDTO);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(storeInfoDTO);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("가게 정보 불러오는 도중 에러가 발생했습니다.");
        }
    }

    // 가게 배너 리스트로 담기
    @GetMapping("/{storeNo}/storebanner")
    @ResponseBody
    public ResponseEntity<?> getBannerList(@PathVariable("storeNo") long storeNo,StoreInfoDTO storeInfoDTO) {
        try{
            BannerDTO bannerDTO = storeService.getBannerList(storeNo);
            List<String> bannerList = bannerDTO.getBannerList();

            List<String> fileUrls = new ArrayList<>();
            for (String bannerName : bannerList) {
                fileUrls.add(bannerName);
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(fileUrls);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("배너 정보를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    // 가게 배너 파일 서버에서 로컬 접근 및 불러오기
    @GetMapping("/{storeNo}/api/files")
    public ResponseEntity<byte[]> getBanner(@PathVariable("storeNo") long storeNo, @RequestParam("filename") String filename) {
        try {
            if (filename == null || filename.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("유효하지 않은 가게 배너 파일명입니다.".getBytes());
            }

            String imageUrlWithId = "https://drive.google.com/uc?id=" + filename;

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<byte[]> response = restTemplate.exchange(imageUrlWithId, HttpMethod.GET, null, byte[].class);

            HttpHeaders headers = new HttpHeaders();
            String contentType = response.getHeaders().getContentType() != null ?
                    response.getHeaders().getContentType().toString() :
                    "application/octet-stream";

            headers.set("Content-Type", contentType);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(response.getBody());

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("배너 이미지를 불러오는 도중에 오류가 발생했습니다.".getBytes());
        }
    }

    // 가게 프로필 DTO에 담기
    @GetMapping("/{storeNo}/storeProfile")
    public ResponseEntity<?> getProfileName(@PathVariable("storeNo") long storeNo, StoreInfoDTO storeInfoDTO){
        try{

            storeInfoDTO = storeService.getStoreInfo(storeNo);
            String profileName = storeInfoDTO.getStoreProfile();
    //        System.out.println(profileName);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(profileName);
        } catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("가게 프로필 정보를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    // 가게 프로필 파일 서버에서 로컬 접근 및 불러오기
    @GetMapping(value = "/{storeNo}/api/profile")
    public ResponseEntity<byte[]> getProfile(@PathVariable("storeNo") long storeNo, @RequestParam("profileName") String profileName){
        try {
            if (profileName == null || profileName.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("유효하지 않은 profileName입니다.".getBytes());
            }

            String imageUrlWithId = "https://drive.google.com/uc?id=" + profileName;

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<byte[]> response = restTemplate.exchange(imageUrlWithId, HttpMethod.GET, null, byte[].class);

            HttpHeaders headers = new HttpHeaders();
            String contentType = response.getHeaders().getContentType() != null ?
                    response.getHeaders().getContentType().toString() :
                    "application/octet-stream";

            headers.set("Content-Type", contentType);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(response.getBody());

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("가게 프로필 이미지 불러오는 중 오류가 발생했습니다.".getBytes());
        }
    }

    // 메뉴 이미지 DB에서 불러오기 및 DTO에 담기
    @GetMapping("/{storeNo}/storeMenu")
    public ResponseEntity<?> getMenuName(@PathVariable("storeNo") long storeNo, StoreInfoDTO storeInfoDTO){
        try{
            storeInfoDTO = storeService.getStoreInfo(storeNo);
            String menuName = storeInfoDTO.getStoreMenu();

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(menuName);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("메뉴 이미지 불러오는 도중 에러가 발생했습니다.");
        }
    }

    // 메뉴 이미지 서버에서 로컬 접근 및 불러오기
    @GetMapping("/{storeNo}/api/menu")
    public ResponseEntity<byte[]> getMenu(@PathVariable("storeNo") long storeNo, @RequestParam("menuName") String menuName ){
        try {
            if (menuName == null || menuName.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("유효하지 않은 menuName입니다.".getBytes());
            }

            String imageUrlWithId = "https://drive.google.com/uc?id=" + menuName;

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<byte[]> response = restTemplate.exchange(imageUrlWithId, HttpMethod.GET, null, byte[].class);

            HttpHeaders headers = new HttpHeaders();
            String contentType = response.getHeaders().getContentType() != null ?
                    response.getHeaders().getContentType().toString() :
                    "application/octet-stream";

            headers.set("Content-Type", contentType);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(response.getBody());

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("메뉴 이미지 불러오는 도중 오류가 발생했습니다.".getBytes());
        }
    }

    // 리뷰 정보 DB에서 가져오기
    @GetMapping(value = "/{storeNo}/review", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public ResponseEntity<?> getReviewInfo(@PathVariable("storeNo") long storeNo, ModelAndView mv, @ModelAttribute ReviewDTO reviewDTO, StoreInfoDTO storeInfoDTO, ReviewContentDTO reviewContentDTO){
        try{

    //        System.out.println("리뷰 조회 매퍼 옴.");
            reviewDTO = storeService.getReviewList(storeNo);
            System.out.println("reviewDTO : " + reviewDTO);

            mv.addObject("review 정보", reviewDTO);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(reviewDTO);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("리뷰 정보를 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @GetMapping(value = "/{storeNo}/reviewscope", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public ResponseEntity<?> getReviewInfoByScope(@PathVariable("storeNo") long storeNo, ModelAndView mv, @ModelAttribute ReviewDTO reviewDTO, StoreInfoDTO storeInfoDTO, ReviewContentDTO reviewContentDTO){
        try{

    //        System.out.println("리뷰 조회 매퍼 옴.");
            reviewDTO = storeService.getReviewListByScope(storeNo);
            System.out.println("reviewDTO : " + reviewDTO);

            mv.addObject("review 정보", reviewDTO);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(reviewDTO);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("리뷰 불러오는 도중 에러가 발생했습니다.");
        }
    }

    @GetMapping(value = "/{storeNo}/reviewSecondScope", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public ResponseEntity<?> getReviewInfoByLowScope(@PathVariable("storeNo") long storeNo, ModelAndView mv, @ModelAttribute ReviewDTO reviewDTO, StoreInfoDTO storeInfoDTO, ReviewContentDTO reviewContentDTO){
        try{
    //        System.out.println("리뷰 조회 매퍼 옴.");
            reviewDTO = storeService.getReviewListByLowScope(storeNo);
            System.out.println("reviewDTO : " + reviewDTO);

            mv.addObject("review 정보", reviewDTO);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(reviewDTO);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("리뷰 불러오는 도중에 에러가 발생했습니다.");
        }
    }

    // 서버에서 드라이브에 있는 리뷰 사진 불러오기
    @GetMapping("/{storeNo}/api/reviewImg")
    @ResponseBody
    public ResponseEntity<byte[]> getReviewImg(@PathVariable("storeNo") long storeNo, @RequestParam("reviewImgName") String reviewImgName ){
        try {
            if (reviewImgName == null || reviewImgName.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("유효하지 않은 fileId입니다.".getBytes());
            }

            String imageUrlWithId = "https://drive.google.com/uc?id=" + reviewImgName;

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<byte[]> response = restTemplate.exchange(imageUrlWithId, HttpMethod.GET, null, byte[].class);

            HttpHeaders headers = new HttpHeaders();
            String contentType = response.getHeaders().getContentType() != null ?
                    response.getHeaders().getContentType().toString() :
                    "application/octet-stream";

            headers.set("Content-Type", contentType);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("이미지 다운로드 중 오류가 발생했습니다.".getBytes());
        }
    }

    // 서버에서 로컬에 있는 사용자 프로필 이미지 불러오기
    @GetMapping("/{storeNo}/api/userProfile")
    @ResponseBody
    public ResponseEntity<byte[]> getUserProfile(@PathVariable("storeNo") long storeNo, @RequestParam("userProfileName") String userProfileName ){
        try {
            if (userProfileName == null || userProfileName.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("유효하지 않은 fileId입니다.".getBytes());
            }

            String imageUrlWithId = "https://drive.google.com/uc?id=" + userProfileName;

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<byte[]> response = restTemplate.exchange(imageUrlWithId, HttpMethod.GET, null, byte[].class);

            HttpHeaders headers = new HttpHeaders();
            String contentType = response.getHeaders().getContentType() != null ?
                    response.getHeaders().getContentType().toString() :
                    "application/octet-stream";

            headers.set("Content-Type", contentType);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(response.getBody());

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("이미지 다운로드 중 오류가 발생했습니다.".getBytes());
        }
    }

    // 리뷰 등록하기
    @PostMapping(value="/{storeNo}/review", consumes = "multipart/form-data")
    @ResponseBody
    public ResponseEntity<?> createReview(@PathVariable("storeNo") long storeNo, @RequestParam("params") String params, @RequestPart(value = "reviewImage", required = false) MultipartFile singleFile) {
        try{
            System.out.println("리뷰 등록하러 왔다.");

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> paramMap;
            ReviewContentDTO reviewContentDTO = new ReviewContentDTO();

            try {
                paramMap = objectMapper.readValue(params, Map.class);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR). body("리뷰 정보 파싱 중 에러가 발생했습니다.");
            }

            System.out.println("리뷰 정보들 : " + paramMap);
            System.out.println("리뷰 사진" + singleFile);

            paramMap.forEach((key, value) -> {
                try {
                    String strValue = value.toString(); // 값이 문자열로 변환된 상태에서 작업을 시작
                    switch (key) {
                        case "reviewDate":
                            reviewContentDTO.setReviewDate(strValue.isEmpty() ? null : strValue);
                            break;
                        case "reviewContent":
                            reviewContentDTO.setReviewContent(strValue.isEmpty() ? null : strValue);
                            break;
                        case "reviewScope":
                            reviewContentDTO.setReviewScope(strValue.isEmpty() ? null : strValue);
                            break;
                        case "storeNo":
                            reviewContentDTO.setStoreNo(strValue.isEmpty() ? null : Long.parseLong(strValue));
                            break;
                        case "userNo":
                            reviewContentDTO.setUserNo(strValue.isEmpty() ? null : Long.parseLong(strValue));
                            break;
                        case "resNo":
                            reviewContentDTO.setResNo(strValue.isEmpty() ? null : Long.parseLong(strValue));
                            break;
                    }
                    System.out.println("Parsed value for key: " + key + ", value: " + strValue);
                } catch (NumberFormatException e) {
                    System.err.println("Value format error for key: " + key + ", value: " + value);
                    e.printStackTrace();
                }
            });

            // 파일 업로드가 있을 때만 파일명 커스텀 및 업로드 처리
            if (singleFile != null && !singleFile.isEmpty()) {
                // 파일명 커스텀하기 ex)REVIEW2401022
                Date nowDate = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String today = simpleDateFormat.format(nowDate);

                long reviewImageCount = Long.parseLong(storeService.getReviewCount(today)) + 1;

                // reviewImage 필드 값 설정
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyMMdd");

                String reviewDate = simpleDateFormat2.format(nowDate).toString();

                String reviewImageValue = "REVIEW" + reviewDate;

                String fileId = googleDriveService.uploadFile(singleFile, INQUIRY_FOLDER_ID, reviewImageValue);
//                String fileId = googleDriveService.uploadFile(singleFile, REVIEW_FOLDER_ID, reviewImageValue);
                System.out.println("fileId : " + fileId);
                reviewContentDTO.setReviewImage(fileId);

            } else {
                // 이미지가 없을 경우 기본 값을 설정
                reviewContentDTO.setReviewImage(null);
            }

            storeService.createReview(reviewContentDTO);

            // 리뷰 달기 완성 후 유저 활동 +1 늘리기
            storeService.increaseReview(reviewContentDTO.getUserNo());

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("message", "리뷰 등록에 성공했습니다.");

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(responseMap);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("리뷰 등록하는 도중에 에러가 발생했습니다.");
        }
    }

    // 리뷰 삭제하기
    @DeleteMapping("/{storeNo}/deletereview")
    public ResponseEntity<?> deleteReview(@PathVariable("storeNo") long storeNo, @RequestParam("reviewNo") long reviewNo, @RequestParam("userNo") long userNo){
        try {
            System.out.println("리뷰 삭제 옴");
            System.out.println("zz");

            // 삭제할 리뷰 정보 가져오기(리뷰 이미지 포함)
            ReviewContentDTO reviewContentDTO = storeService.getReviewContent(reviewNo);
            System.out.println("reviewContentDTO = " + reviewContentDTO);

            // 리뷰 삭제
            storeService.deleteReview(reviewNo);

            // 리뷰 이미지 파일 삭제
            if (reviewContentDTO.getReviewImage() != null && !reviewContentDTO.getReviewImage().isEmpty()) {
                try {
                    googleDriveService.deleteFile(reviewContentDTO.getReviewImage()); // Google Drive에서 파일 삭제
                    System.out.println("구글 드라이브 파일에 있던 기존 파일 제거했습니다: " + reviewContentDTO.getReviewImage());
                } catch (Exception e) {
                    System.out.println("파일 삭제 실패: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("기본 이미지이므로 삭제하지 않습니다: " + reviewContentDTO.getReviewImage());
            }

            // 리뷰 달기 완성 후 유저 활동 - 1 감소
            storeService.decreaseReview(userNo);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("message", "리뷰 삭제에 성공했습니다.");

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(responseMap);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("리뷰 삭제 도중에 에러가 발생했습니다.");
        }
    }

    // 리뷰 작성하기 버튼 활성화를 위한 리뷰 작성 권환 확인용 -> 예약 tbl에서 해당 아이디, 가게번호 넘겨서 확인하기
    @GetMapping("/{storeNo}/getreviewlist")
    public ResponseEntity<?> getUserReviewList(@PathVariable("storeNo") long storeNum, @RequestParam("userId") String userId, @RequestParam("storeNo") long storeNo, Model model){
        try {
            //        System.out.println("리뷰 권한 넘어옴");
            //        System.out.println("userId : " + userId + " , storeNo : " + storeNo);

            List<ReservationInfoDTO> reservationList = new ArrayList<>();

            reservationList = storeService.getUserReviewList(userId, storeNo);

            //        System.out.println("reservationList = " + reservationList);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(reservationList);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("리뷰 작성 권한 확인 도중 에러가 발생했습니다.");
        }
    }

    // 리뷰 버튼 활성화를 위한 리뷰 작성 권한 확인용 -> 리뷰 tbl에서 예약 번호 확인하기
    @GetMapping("/{storeNo}/checkReviewList")
    public ResponseEntity<?> checkReviewList(@PathVariable("storeNo") long storeNo, @RequestParam("resNo") long resNo){
        try {
            //        System.out.println("리뷰 권한2 넘어옴");

            boolean result = storeService.checkReviewList(resNo);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(result);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("리뷰 작성 권환 확인 도중 에러가 발생했습니다.");
        }
    }

    // 예약 가능 인원 조회
    @GetMapping("/{storeNo}/resPosNumber")
    public ResponseEntity<?> getResPosNumber(@PathVariable("storeNo") long storeNum, @RequestParam("storeNo") long storeNo, @RequestParam("day") String day, @RequestParam("date") String date, @ModelAttribute StoreResPosNumDTO storeResPosNumDTO) {
        try {
            System.out.println("예약 가능 인원이요~~~");

            switch (day) {
                case "0":
                    storeResPosNumDTO.setrDay("SUNDAY");
                    break;
                case "1":
                    storeResPosNumDTO.setrDay("MONDAY");
                    break;
                case "2":
                    storeResPosNumDTO.setrDay("TUESDAY");
                    break;
                case "3":
                    storeResPosNumDTO.setrDay("WEDNESDAY");
                    break;
                case "4":
                    storeResPosNumDTO.setrDay("THURSDAY");
                    break;
                case "5":
                    storeResPosNumDTO.setrDay("FRIDAY");
                    break;
                case "6":
                    storeResPosNumDTO.setrDay("SATURDAY");
                    break;
            }

            storeResPosNumDTO.setStoreNo(storeNo);
            storeResPosNumDTO.setReservationDate(LocalDate.parse(date));

            System.out.println("예약 가능 ??? " + storeResPosNumDTO);

            List<DayResPosNumDTO> listDayResPosNum = storeService.getResPosNum(storeResPosNumDTO);

            System.out.println("listDayResPosNum = " + listDayResPosNum);

            //        storeResPosNumDTO.setListDayResPosNumDTO(listDayResPosNum);
            //        System.out.println("storeResPosNumDTO = " + storeResPosNumDTO);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(listDayResPosNum);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("예약 가능 인원 조회 도중에 에러가 발생했습니다.");
        }
    }



    // 메인
    @GetMapping("/{storeNo}")
    public String viewStorePage(@PathVariable("storeNo") int storeNo) {
        // storeNo에 해당하는 가게 정보 조회

        // 해당 가게의 예약 페이지를 보여주는 뷰 반환
        return "storePage";
    }


    @GetMapping("/search")
    public List<StoreInfoDTO> searchStores(@RequestParam("name") String storeName) {
        // 검색어가 비어있지 않으면, 검색어를 TBL_SEARCH 테이블에 저장 또는 업데이트
        if (storeName != null && !storeName.trim().isEmpty()) {
            storeService.insertOrUpdateSearch(storeName);
        }

        // 검색어가 비어있을 경우 예외 처리
        if (storeName == null || storeName.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "검색어를 입력하세요.");
        }

        return storeService.searchStores(storeName);
    }



    @GetMapping("/popular-searches")
    public List<String> getPopularSearches() {
        try {
            return storeService.getPopularSearches();
        } catch (Exception e) {
            e.printStackTrace();  // 서버 로그에 예외 출력
            throw new RuntimeException("인기 검색어를 가져오는 중 오류가 발생했습니다.");
        }
    }

    @PostMapping("/insertOrUpdateSearch")
    public ResponseEntity<String> insertOrUpdateSearch(@RequestBody Map<String, String> searchRequest) {
        String searchWord = searchRequest.get("storeName");

        if (searchWord != null && !searchWord.trim().isEmpty()) {
            // 검색어를 DB에 저장하거나 업데이트
            storeService.insertOrUpdateSearch(searchWord);
            return ResponseEntity.ok("검색어 기록 성공");
        } else {
            return ResponseEntity.badRequest().body("유효하지 않은 검색어입니다.");
        }
    }
}





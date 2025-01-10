package com.ohgiraffers.ukki.store.controller;

import com.ohgiraffers.ukki.store.model.dto.*;
import com.ohgiraffers.ukki.store.model.service.StoreService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping(value="/store")
@CrossOrigin(origins = "http://localhost:3000")
public class StoreController {

    private final StoreService storeService;
    private final String SHARED_FOLDER = "\\\\I7E-74\\ukki_nas\\store";
//    private final String SHARED_FOLDER = "\\\\Desktop-43runa1\\images";

    public StoreController(StoreService storeService){
        this.storeService = storeService;
    }

    // 검색 페이지 만들어지면 pathvariable로 변경하기
    @GetMapping(value="/test", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public StoreInfoDTO getStoreInfo(ModelAndView mv, @ModelAttribute StoreInfoDTO storeInfoDTO){

        // storeDB 연결
//        System.out.println("getStoreInfo 넘어옴");
        storeInfoDTO = storeService.getStoreInfo(storeInfoDTO);


//        System.out.println("keyword 연결 전" + storeInfoDTO);

        // keywordDB 연결
        KeywordDTO keywordDTO = storeService.getKeyword(storeInfoDTO);
        storeInfoDTO.setStoreKeyword(keywordDTO);

        // operationDB 연결
        OperationDTO operationDTO = storeService.getOperation(storeInfoDTO);
        storeInfoDTO.setOperationTime(operationDTO);

        mv.addObject("getStoreInfo", storeInfoDTO);

//        System.out.println(keywordDTO);
//        System.out.println("keyword연결 이후" + storeInfoDTO);

        return storeInfoDTO;
    }

    // 가게 배너 리스트로 담기
    @GetMapping("/storebanner/5")
    @ResponseBody
    public List<String> getBannerList(StoreInfoDTO storeInfoDTO) {
        BannerDTO bannerDTO = storeService.getBannerList(storeInfoDTO);
        List<String> bannerList = bannerDTO.getBannerList();

        List<String> fileUrls = new ArrayList<>();
        for (String bannerName : bannerList) {
            fileUrls.add(bannerName);
        }
        return fileUrls;
    }

    // 가게 배너 파일 서버에서 로컬 접근 및 불러오기
    @GetMapping("/api/files")
    public ResponseEntity<Resource> getBanner(@RequestParam("filename") String filename) {
        try {
            // Paths.get(SHARED_FOLDER)는 SHARED_FOLDER로 지정한 경로(여기서는 전역필드로 초기화) 문자열을 Path 객체로 변환
            // 이 Path 객체는 파일 시스템 경로를 추상화하여 처리할 수 있게 한다.
            // .resolve(filename) : filename은 파일 이름을 나타내는 문자열.
            // ,resolve(filename) 메소드는 Path 객체에 파일 이름을 결합하여 새로운 경로를 생성한다.
            // 이 결합된 경로는 SHATED_FOLDER 경로와 filename을 합쳐서 완전한 파일 경로를 만든다.
            // 즉, 최종 경로가 \\\\192.168.0.138\\ukki_nas\\store\\5banner1 이 된다
            Path file = Paths.get(SHARED_FOLDER).resolve(filename + ".png");

            // 디버깅 확인
//            System.out.println("file 경로 : " + file.toString());


            // 자바에서 Path 객체로부터 Resource 객체를 생성하는 부분
            // file.toUri() :
            // file은 Path 객체로 파일 시스템 상의 특정 파일을 나타낸다.
            // toUri() 메소드는 이 Path객체를 URI(Uniform Resource Identifier) 객체로 변환한다. URI는 파일 위치를 나타내는 문자열 형식의 주소이다.
            // new UriResource(uri) :
            // UriResource는 스프링 프레임워크에서 제공하는 클래스 중 하나로, 주어진 URI를 이용해 리소스를 나타낸다.
            // new UriResource(uri)는 지정된 URI를 기반으로 UriResource 객체를 생성한다. 이 객체는 주어진 URI가 가리키는 자원을 나타낸다.
            // 이 UriResource 객체를 사용하여 파일을 읽거나 다른 작업을 수행할 수 있다.
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // 가게 프로필 DTO에 담기
    @GetMapping(value = "/storeProfile/5")
    public ResponseEntity<String> getProfileName(StoreInfoDTO storeInfoDTO){

        storeInfoDTO = storeService.getStoreInfo(storeInfoDTO);
        String profileName = storeInfoDTO.getStoreProfile();
//        System.out.println(profileName);
        return ResponseEntity.ok(profileName);
    }

    // 가게 프로필 파일 서버에서 로컬 접근 및 불러오기
    @GetMapping(value = "/api/profile")
    public ResponseEntity<Resource> getProfile(@RequestParam("profileName") String profileName){

        try {
            Path file = Paths.get(SHARED_FOLDER).resolve(profileName + ".png");
            // 디버깅 확인
//            System.out.println("프로필 파일 경로 : " + file);
            Resource resource = new UrlResource(file.toUri());

            if(resource.exists() && resource.isReadable()){
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; profileName=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // 메뉴 이미지 DB에서 불러오기 및 DTO에 담기
    @GetMapping(value = "/storeMenu/5")
    public ResponseEntity<String> getMenuName(StoreInfoDTO storeInfoDTO){

        storeInfoDTO = storeService.getStoreInfo(storeInfoDTO);
        String menuName = storeInfoDTO.getStoreMenu();

        return ResponseEntity.ok(menuName);
    }

    // 메뉴 이미지 서버에서 로컬 접근 및 불러오기
    @GetMapping(value = "/api/menu")
    public ResponseEntity<Resource> getMenu(@RequestParam("menuName") String menuName ){

        try {
            Path file = Paths.get(SHARED_FOLDER).resolve(menuName + ".png");
//            System.out.println("menu : " + file );
            Resource resource = new UrlResource(file.toUri());

            if(resource.exists() && resource.isReadable()){
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; menuName=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // 리뷰 정보 DB에서 가져오기
    @GetMapping(value = "/review", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public ReviewDTO getReviewInfo(ModelAndView mv, @ModelAttribute ReviewDTO reviewDTO, StoreInfoDTO storeInfoDTO, ReviewContentDTO reviewContentDTO){

//        System.out.println("리뷰 조회 매퍼 옴.");
        reviewDTO = storeService.getReviewList(storeInfoDTO);
        System.out.println("reviewDTO : " + reviewDTO);

        mv.addObject("review 정보", reviewDTO);

        return reviewDTO;
    }

    @GetMapping(value = "/reviewscope", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public ReviewDTO getReviewInfoByScope(ModelAndView mv, @ModelAttribute ReviewDTO reviewDTO, StoreInfoDTO storeInfoDTO, ReviewContentDTO reviewContentDTO){

//        System.out.println("리뷰 조회 매퍼 옴.");
        reviewDTO = storeService.getReviewListByScope(storeInfoDTO);
        System.out.println("reviewDTO : " + reviewDTO);

        mv.addObject("review 정보", reviewDTO);

        return reviewDTO;
    }

    // 서버에서 로컬에 있는 리뷰 사진 불러오기
    @GetMapping("/api/reviewImg")
    @ResponseBody
    public ResponseEntity<Resource> getReviewImg(@RequestParam("reviewImgName") String reviewImgName ){

//        System.out.println("리뷰 이미지 api");
        try {
            Path file = Paths.get(SHARED_FOLDER).resolve(reviewImgName + ".png");
//            System.out.println("reviewImg : " + file );
            Resource resource = new UrlResource(file.toUri());

            if(resource.exists() && resource.isReadable()){
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; reviewImgName=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // 서버에서 로컬에 있는 사용자 프로필 이미지 불러오기
    @GetMapping("/api/userProfile")
    @ResponseBody
    public ResponseEntity<Resource> getUserProfile(@RequestParam("userProfileName") String userProfileName ){

//        System.out.println("사용자 프로필 이미지 api");
        try {
            Path file = Paths.get(SHARED_FOLDER).resolve(userProfileName + ".png");
//            System.out.println("유저 프로필 : " + file );
            Resource resource = new UrlResource(file.toUri());

            if(resource.exists() && resource.isReadable()){
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; userProfileName=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // 리뷰 등록하기
    @PostMapping(value="/5/review", consumes = "multipart/form-data")
    @ResponseBody
    public void createReview(@RequestParam("params") String params, @RequestPart("reviewImage") MultipartFile singleFile) {

        System.out.println("리뷰 등록하러 왔다.");

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> paramMap;
        ReviewContentDTO reviewContentDTO = new ReviewContentDTO();

        try {
            paramMap = objectMapper.readValue(params, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        paramMap.forEach((key, value) -> {
            switch (key) {
                case "reviewDate":
                    reviewContentDTO.setReviewDate(value);
                    break;
                case "reviewContent":
                    reviewContentDTO.setReviewContent(value);
                    break;
                case "reviewScope":
                    reviewContentDTO.setReviewScope(Integer.parseInt(value));
                    break;
                case "storeNo":
                    reviewContentDTO.setStoreNo(Long.parseLong(value));
                    break;
                case "userNo":
                    reviewContentDTO.setUserNo(Long.parseLong(value));
                    break;
                case "resNo":
                    reviewContentDTO.setResNo(Long.parseLong(value));
                    break;
            }
        });

        // 파일명 커스텀하기 ex)REVIEW2401022
        Date nowDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = simpleDateFormat.format(nowDate);

        long reviewImageCount = Long.parseLong(storeService.getReviewCount(today)) + 1;

        // reviewImage 필드 값 설정
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyMMdd");
//        System.out.println("simpleDateFormat2 = " + simpleDateFormat2);

        String reviewDate = simpleDateFormat2.format(nowDate).toString();
//        System.out.println("reviewDate = " + reviewDate);

        String reviewImageValue = "REVIEW" + reviewDate;
//        System.out.println("reviewImageValue = " + reviewImageValue);

        reviewContentDTO.setReviewImage(reviewImageValue + reviewImageCount);
//        System.out.println("reviewContentDTO : " + reviewContentDTO);

        storeService.createReview(reviewContentDTO);

        // 파일 업로드하기
        String filePath = SHARED_FOLDER;
        File dir = new File(filePath);
        if(!dir.exists()){
            dir.mkdirs();
        }

        // 파일명을 reviewContentDTO.getReviewImage()와 같은 값으로 저장
        String originFileName = singleFile.getOriginalFilename();
        String ext = originFileName.substring(originFileName.lastIndexOf('.'));
        String savedName = reviewContentDTO.getReviewImage() + ext;
        String fullPath = filePath + "/" + savedName;
//        System.out.println("리뷰 이미지 file 경로 : " + fullPath);

        try {
            //파일 저장
            singleFile.transferTo(new File(fullPath));
//            System.out.println("파일 저장 완료");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        // 리뷰 달기 완성 후 유저 활동 +1 늘리기
        storeService.increaseReview(reviewContentDTO.getUserNo());


    }


    // 리뷰 작성하기 버튼 활성화를 위한 리뷰 작성 권환 확인용 -> 예약 tbl에서 해당 아이디, 가게번호 넘겨서 확인하기
    @GetMapping(value = "/getreviewlist")
    public ResponseEntity<List<ReservationInfoDTO>> getUserReviewList(@RequestParam("userId") String userId, @RequestParam("storeNo") long storeNo, Model model, @ModelAttribute List<ReservationInfoDTO> reservationList){
//        System.out.println("리뷰 권한 넘어옴");
//        System.out.println("userId : " + userId + " , storeNo : " + storeNo);

        reservationList = storeService.getUserReviewList(userId, storeNo);

//        System.out.println("reservationList = " + reservationList);

        return ResponseEntity.ok(reservationList);

    }

    // 리뷰 버튼 활성화를 위한 리뷰 작성 권한 확인용 -> 리뷰 tbl에서 예약 번호 확인하기
    @GetMapping(value = "/checkReviewList")
    public ResponseEntity<Boolean> checkReviewList(@RequestParam("resNo") long resNo){

//        System.out.println("리뷰 권한2 넘어옴");

        boolean result = storeService.checkReviewList(resNo);

        return ResponseEntity.ok(result);
    }
}


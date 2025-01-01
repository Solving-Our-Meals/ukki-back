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

@RestController
@RequestMapping(value="/store")
@CrossOrigin(origins = "http://localhost:3000/store")
public class StoreController {

    private final StoreService storeService;
//    private final String SHARED_FOLDER = "\\\\I7E-74\\ukki_nas\\store";
    private final String SHARED_FOLDER = "\\\\Desktop-43runa1\\images";

    public StoreController(StoreService storeService){
        this.storeService = storeService;
    }

    // 검색 페이지 만들어지면 pathvariable로 변경하기
    @GetMapping(value="/test", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public StoreInfoDTO getStoreInfo(ModelAndView mv, @ModelAttribute StoreInfoDTO storeInfoDTO){

        // storeDB 연결
        System.out.println("getStoreInfo 넘어옴");
        storeInfoDTO = storeService.getStoreInfo(storeInfoDTO);


        System.out.println("keyword 연결 전" + storeInfoDTO);

        // keywordDB 연결
        KeywordDTO keywordDTO = storeService.getKeyword(storeInfoDTO);
        storeInfoDTO.setStoreKeyword(keywordDTO);

        // operationDB 연결
        OperationDTO operationDTO = storeService.getOperation(storeInfoDTO);
        storeInfoDTO.setOperationTime(operationDTO);

        mv.addObject("getStoreInfo", storeInfoDTO);

        System.out.println(keywordDTO);
        System.out.println("keyword연결 이후" + storeInfoDTO);

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
            System.out.println("file 경로 : " + file.toString());


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
        System.out.println(profileName);
        return ResponseEntity.ok(profileName);
    }

    // 가게 프로필 파일 서버에서 로컬 접근 및 불러오기
    @GetMapping(value = "/api/profile")
    public ResponseEntity<Resource> getProfile(@RequestParam("profileName") String profileName){

        try {
            Path file = Paths.get(SHARED_FOLDER).resolve(profileName + ".png");
            // 디버깅 확인
            System.out.println("프로필 파일 경로 : " + file);
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
            System.out.println("menu : " + file );
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

        System.out.println("리뷰 조회 매퍼 옴.");
        reviewDTO = storeService.getReviewList(storeInfoDTO);
        System.out.println("reviewDTO : " + reviewDTO);

        mv.addObject("review 정보", reviewDTO);

        return reviewDTO;
    }

    // 서버에서 로컬에 있는 리뷰 사진 불러오기
    @GetMapping("/api/reviewImg")
    @ResponseBody
    public ResponseEntity<Resource> getReviewImg(@RequestParam("reviewImgName") String reviewImgName ){

        System.out.println("리뷰 이미지 api");
        try {
            Path file = Paths.get(SHARED_FOLDER).resolve(reviewImgName + ".png");
            System.out.println("reviewImg : " + file );
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

        System.out.println("사용자 프로필 이미지 api");
        try {
            Path file = Paths.get(SHARED_FOLDER).resolve(userProfileName + ".png");
            System.out.println("유저 프로필 : " + file );
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
    @PostMapping(value="/5/review", consumes = "multipart/form-data", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public void createReview(Model model, @RequestPart("params") String params, @RequestPart("reviewImage") MultipartFile singleFile, ReviewContentDTO reviewContentDTO) {

        System.out.println("리뷰 등록하러 왔다.");

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> paramMap;

        System.out.println("Params: " + params);
        System.out.println("File: " + singleFile);

        try {
            paramMap = objectMapper.readValue(params, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Date nowDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = simpleDateFormat.format(nowDate);

        long reviewImageCount = Long.parseLong(storeService.getReviewCount(today)) + 1;
        System.out.println("리뷰 이미지 갯수 " + reviewImageCount);

        StringBuilder sb = new StringBuilder();

        paramMap.forEach((key, value) -> {
            switch (key) {
                case "reviewDate":
                    reviewContentDTO.setReviewDate(value);
                    break;
                case "reviewContent":
                    reviewContentDTO.setReviewContent(value);
                    break;
                case "reviewImage":
                    reviewContentDTO.setReviewImage(value + reviewImageCount);
                    break;
                case "userNo":
                    reviewContentDTO.setUserNo(Long.parseLong(value));
                    break;
                case "storeNo":
                    reviewContentDTO.setStoreNo(Long.parseLong(value));
                    break;
            }
        });

        System.out.println("2222" + reviewContentDTO);

        storeService.createReview(reviewContentDTO);

        // 파일 업로드하기
        // 서버로 전달 된 File을 서버에서 설정하는 경로에 저장한다.
//        String filePath = SHARED_FOLDER;
//        File dir = new File(filePath);
//        System.out.println("파일 경로인건가...? : " + dir.getAbsolutePath());
//
//        if(!dir.exists()){
//            /* mkdirs() : 해당 경로에 디렉토리가 없을 경우 생성해주는 메소드이다. */
//            dir.mkdirs();
//        }
//
//        // 파일명 변경 처리
//        String originFileName = singleFile.getOriginalFilename();
//        String ext = originFileName.substring(originFileName.lastIndexOf("."));
//        System.out.println("ext = " + ext);
//
//        // UUID : 식별 가능한 고유의 아이디값을 생성해주는 유틸성 클래스
//        String savedName = UUID.randomUUID() + ext;
//        System.out.println("savedName = " + savedName);
//
//        // 파일 저장
//        try {
//            singleFile.transferTo(new File(filePath + "/" + savedName));
//            model.addAttribute("message", "파일 업로드 성공");
//        } catch (IOException e) {
//            model.addAttribute("message", "파일 업로드 실패");
//        }

    }




//    @PostMapping(value="/5/review", consumes = "multipart/form-data")
//    @ResponseBody
//    public void createReview(@RequestPart("params") Map<String, String> params, @RequestPart("reviewImage") MultipartFile singleFile, ReviewContentDTO reviewContentDTO) {
//
//        Date nowDate = new Date();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String today = simpleDateFormat.format(nowDate);
//
//        long reviewImageCount = Long.parseLong(storeService.getReviewCount(today)) + 1;
//        System.out.println("리뷰 이미지 갯수 " + reviewImageCount);
//
//        StringBuilder sb = new StringBuilder();
//
//        params.entrySet().forEach(entry -> {
//            sb.append(entry.getKey() + " = " + entry.getValue() + "\n");
//            switch (entry.getKey()) {
//                case "reviewDate":
//                    reviewContentDTO.setReviewDate(entry.getValue());
//                    break;
//                case "reviewContent":
//                    reviewContentDTO.setReviewContent(entry.getValue());
//                    break;
//                case "reviewImage":
//                    reviewContentDTO.setReviewImage(entry.getValue() + reviewImageCount);
//                    break;
//                case "userNo":
//                    reviewContentDTO.setUserNo(Long.parseLong(entry.getValue()));
//                    break;
//                case "storeNo":
//                    reviewContentDTO.setStoreNo(Long.parseLong(entry.getValue()));
//                    break;
//            }
//        });
//
//        storeService.createReview(reviewContentDTO);
//
//        String filePath = SHARED_FOLDER;
//        File dir = new File(filePath);
//        System.out.println("파일 경로인건가...? : " + dir.getAbsolutePath());
//
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }
//
//        String originFileName = singleFile.getOriginalFilename();
//        String ext = originFileName.substring(originFileName.lastIndexOf("."));
//        System.out.println("ext = " + ext);
//
//        String savedName = UUID.randomUUID() + ext;
//        System.out.println("savedName = " + savedName);
//
//
//        try {
//            singleFile.transferTo(new File(filePath + "/" + savedName));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//    }



//    @PostMapping(value = "/5/review", consumes = "multipart/form-data")
//    @ResponseBody
//    public void createReview(@RequestPart("params") String params, @RequestPart("reviewImage") MultipartFile singleFile, ReviewContentDTO reviewContentDTO) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        Map<String, String> paramMap;
//
//        try {
//            paramMap = objectMapper.readValue(params, Map.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return;
//        }
//
//        Date nowDate = new Date();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String today = simpleDateFormat.format(nowDate);
//
//        long reviewImageCount = Long.parseLong(storeService.getReviewCount(today)) + 1;
//        System.out.println("리뷰 이미지 갯수 " + reviewImageCount);
//
//        paramMap.forEach((key, value) -> {
//            switch (key) {
//                case "reviewDate":
//                    reviewContentDTO.setReviewDate(value);
//                    break;
//                case "reviewContent":
//                    reviewContentDTO.setReviewContent(value);
//                    break;
//                case "reviewImage":
//                    reviewContentDTO.setReviewImage(value + reviewImageCount);
//                    break;
//                case "userNo":
//                    reviewContentDTO.setUserNo(Long.parseLong(value));
//                    break;
//                case "storeNo":
//                    reviewContentDTO.setStoreNo(Long.parseLong(value));
//                    break;
//            }
//        });
//
//        storeService.createReview(reviewContentDTO);
//
//        String SHARED_FOLDER = "\\\\Desktop-43runa1\\images";
//        String filePath = SHARED_FOLDER;
//        File dir = new File(filePath);
//        System.out.println("파일 경로인건가...? : " + dir.getAbsolutePath());
//
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }
//
//        String originFileName = singleFile.getOriginalFilename();
//        String ext = originFileName.substring(originFileName.lastIndexOf("."));
//        System.out.println("ext = " + ext);
//
//        String savedName = UUID.randomUUID() + ext;
//        System.out.println("savedName = " + savedName);
//
//        try {
//            singleFile.transferTo(new File(filePath + "/" + savedName));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

}


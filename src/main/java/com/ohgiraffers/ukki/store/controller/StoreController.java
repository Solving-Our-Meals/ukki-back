package com.ohgiraffers.ukki.store.controller;

import com.ohgiraffers.ukki.store.model.dto.*;
import com.ohgiraffers.ukki.store.model.service.BossService;
import com.ohgiraffers.ukki.store.model.service.StoreService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
@RequestMapping(value="/store")
@CrossOrigin(origins = "http://localhost:3000")
public class StoreController {

    private final StoreService storeService;
    private final String SHARED_FOLDER = "\\\\I7E-74\\ukki_nas\\store";
//    private final String SHARED_FOLDER = "\\\\Desktop-43runa1\\images\\store";


    public StoreController(StoreService storeService, BossService bossService){
        this.storeService = storeService;
    }

    // 검색 페이지 만들어지면 pathvariable로 변경하기
    @GetMapping(value="/{storeNo}/getInfo", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public StoreInfoDTO getStoreInfo(@PathVariable("storeNo") long storeNo, ModelAndView mv, @ModelAttribute StoreInfoDTO storeInfoDTO){

        // storeDB 연결
//        System.out.println("getStoreInfo 넘어옴");
        storeInfoDTO = storeService.getStoreInfo(storeNo);


//        System.out.println("keyword 연결 전" + storeInfoDTO);

        // keywordDB 연결
        KeywordDTO keywordDTO = storeService.getKeyword(storeNo);
        storeInfoDTO.setStoreKeyword(keywordDTO);

        // operationDB 연결
        OperationDTO operationDTO = storeService.getOperation(storeNo);
        storeInfoDTO.setOperationTime(operationDTO);

        mv.addObject("getStoreInfo", storeInfoDTO);

//        System.out.println(keywordDTO);
//        System.out.println("keyword연결 이후" + storeInfoDTO);

        return storeInfoDTO;
    }

    // 가게 배너 리스트로 담기
    @GetMapping("/{storeNo}/storebanner")
    @ResponseBody
    public List<String> getBannerList(@PathVariable("storeNo") long storeNo,StoreInfoDTO storeInfoDTO) {
        BannerDTO bannerDTO = storeService.getBannerList(storeNo);
        List<String> bannerList = bannerDTO.getBannerList();

        List<String> fileUrls = new ArrayList<>();
        for (String bannerName : bannerList) {
            fileUrls.add(bannerName);
        }
        return fileUrls;
    }

    // 가게 배너 파일 서버에서 로컬 접근 및 불러오기
    @GetMapping("/{storeNo}/api/files")
    public ResponseEntity<Resource> getBanner(@PathVariable("storeNo") long storeNo, @RequestParam("filename") String filename) {
        try {
            // Paths.get(SHARED_FOLDER)는 SHARED_FOLDER로 지정한 경로(여기서는 전역필드로 초기화) 문자열을 Path 객체로 변환
            // 이 Path 객체는 파일 시스템 경로를 추상화하여 처리할 수 있게 한다.
            // .resolve(filename) : filename은 파일 이름을 나타내는 문자열.
            // ,resolve(filename) 메소드는 Path 객체에 파일 이름을 결합하여 새로운 경로를 생성한다.
            // 이 결합된 경로는 SHATED_FOLDER 경로와 filename을 합쳐서 완전한 파일 경로를 만든다.
            // 즉, 최종 경로가 \\\\192.168.0.138\\ukki_nas\\store\\5banner1 이 된다
            Path file = Paths.get(SHARED_FOLDER).resolve(filename + ".png");
            if(!Files.exists(file)){
                file = Paths.get(SHARED_FOLDER).resolve(filename + ".jpg");
            }

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
    @GetMapping(value = "/{storeNo}/storeProfile")
    public ResponseEntity<String> getProfileName(@PathVariable("storeNo") long storeNo, StoreInfoDTO storeInfoDTO){

        storeInfoDTO = storeService.getStoreInfo(storeNo);
        String profileName = storeInfoDTO.getStoreProfile();
//        System.out.println(profileName);
        return ResponseEntity.ok(profileName);
    }

    // 가게 프로필 파일 서버에서 로컬 접근 및 불러오기
    @GetMapping(value = "/{storeNo}/api/profile")
    public ResponseEntity<Resource> getProfile(@PathVariable("storeNo") long storeNo, @RequestParam("profileName") String profileName){

        try {
            Path file = Paths.get(SHARED_FOLDER).resolve(profileName + ".png");
            if(!Files.exists(file)){
                file = Paths.get(SHARED_FOLDER).resolve(profileName + ".jpg");
            }

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
    @GetMapping(value = "/{storeNo}/storeMenu")
    public ResponseEntity<String> getMenuName(@PathVariable("storeNo") long storeNo, StoreInfoDTO storeInfoDTO){

        storeInfoDTO = storeService.getStoreInfo(storeNo);
        String menuName = storeInfoDTO.getStoreMenu();

        return ResponseEntity.ok(menuName);
    }

    // 메뉴 이미지 서버에서 로컬 접근 및 불러오기
    @GetMapping(value = "/{storeNo}/api/menu")
    public ResponseEntity<Resource> getMenu(@PathVariable("storeNo") long storeNo, @RequestParam("menuName") String menuName ){

        try {
            Path file = Paths.get(SHARED_FOLDER).resolve(menuName + ".png");
            if(!Files.exists(file)){
                file = Paths.get(SHARED_FOLDER).resolve(menuName + ".jpg");
            }
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
    @GetMapping(value = "/{storeNo}/review", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public ReviewDTO getReviewInfo(@PathVariable("storeNo") long storeNo, ModelAndView mv, @ModelAttribute ReviewDTO reviewDTO, StoreInfoDTO storeInfoDTO, ReviewContentDTO reviewContentDTO){

//        System.out.println("리뷰 조회 매퍼 옴.");
        reviewDTO = storeService.getReviewList(storeNo);
        System.out.println("reviewDTO : " + reviewDTO);

        mv.addObject("review 정보", reviewDTO);

        return reviewDTO;
    }

    @GetMapping(value = "/{storeNo}/reviewscope", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public ReviewDTO getReviewInfoByScope(@PathVariable("storeNo") long storeNo, ModelAndView mv, @ModelAttribute ReviewDTO reviewDTO, StoreInfoDTO storeInfoDTO, ReviewContentDTO reviewContentDTO){

//        System.out.println("리뷰 조회 매퍼 옴.");
        reviewDTO = storeService.getReviewListByScope(storeNo);
        System.out.println("reviewDTO : " + reviewDTO);

        mv.addObject("review 정보", reviewDTO);

        return reviewDTO;
    }

    @GetMapping(value = "/{storeNo}/reviewSecondScope", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public ReviewDTO getReviewInfoByLowScope(@PathVariable("storeNo") long storeNo, ModelAndView mv, @ModelAttribute ReviewDTO reviewDTO, StoreInfoDTO storeInfoDTO, ReviewContentDTO reviewContentDTO){

//        System.out.println("리뷰 조회 매퍼 옴.");
        reviewDTO = storeService.getReviewListByLowScope(storeNo);
        System.out.println("reviewDTO : " + reviewDTO);

        mv.addObject("review 정보", reviewDTO);

        return reviewDTO;
    }

    // 서버에서 로컬에 있는 리뷰 사진 불러오기
    @GetMapping("/{storeNo}/api/reviewImg")
    @ResponseBody
    public ResponseEntity<Resource> getReviewImg(@PathVariable("storeNo") long storeNo, @RequestParam("reviewImgName") String reviewImgName ){

        try {
            Path file = Paths.get(SHARED_FOLDER).resolve(reviewImgName + ".png");
            if(!Files.exists(file)){
                file = Paths.get(SHARED_FOLDER).resolve(reviewImgName + ".jpg");
            }
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
    @GetMapping("/{storeNo}/api/userProfile")
    @ResponseBody
    public ResponseEntity<Resource> getUserProfile(@PathVariable("storeNo") long storeNo, @RequestParam("userProfileName") String userProfileName ){

//        System.out.println("사용자 프로필 이미지 api");
        try {
            Path file = Paths.get(SHARED_FOLDER).resolve(userProfileName + ".png");
            if(!Files.exists(file)){
                file = Paths.get(SHARED_FOLDER).resolve(userProfileName + ".jpg");
            }
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
    @PostMapping(value="/{storeNo}/review", consumes = "multipart/form-data")
    @ResponseBody
    public void createReview(@PathVariable("storeNo") long storeNo, @RequestParam("params") String params, @RequestPart(value = "reviewImage", required = false) MultipartFile singleFile) {

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

        System.out.println("리뷰 정보들 : " + paramMap);
        System.out.println("리뷰 사진" + singleFile);

        paramMap.forEach((key, value) -> {
            switch (key) {
                case "reviewDate":
                    reviewContentDTO.setReviewDate(value.isEmpty() ? null : value);
                    break;
                case "reviewContent":
                    reviewContentDTO.setReviewContent(value.isEmpty() ? null : value);
                    break;
                case "reviewScope":
                    reviewContentDTO.setReviewScope(value.isEmpty() ? null : Integer.parseInt(value));
                    break;
                case "storeNo":
                    reviewContentDTO.setStoreNo(value.isEmpty() ? null : Long.parseLong(value));
                    break;
                case "userNo":
                    reviewContentDTO.setUserNo(value.isEmpty() ? null : Long.parseLong(value));
                    break;
                case "resNo":
                    reviewContentDTO.setResNo(value.isEmpty() ? null : Long.parseLong(value));
                    break;
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

            reviewContentDTO.setReviewImage(reviewImageValue + reviewImageCount);

            // 컴퓨터가 특정 폴더를 찾아서 그 폴더가 없으면 새로 만드는 과정
            // 파일 경로 설정
            String filePath = SHARED_FOLDER;
            // 설정한 파일 경로를 사용해서 File 객체 생성
            File dir = new File(filePath);
            // File 객체가 존재하는지 확인
            if(!dir.exists()){
                // 디렉토리가 존재하지 않으면 모든 필요한 부모 디렉토리를 포함하여 디렉토리를 생성
                dir.mkdirs();
            }

            // 파일명을 reviewContentDTO.getReviewImage()와 같은 값으로 저장
            // singleFile 객체에서 원래 파일 이름 가져와서 originFileName이라는 변수에 저장
            String originFileName = singleFile.getOriginalFilename();
            // 파일 이름에서 마지막 점(.) 이후의 문자열을 확장자로 추출하여 ext라는 변수에 저장
            String ext = originFileName.substring(originFileName.lastIndexOf('.'));
            // 리뷰 이미지 가져와서 앞에서 추출한 확장자와 결합해서 savedName 변수에 저장
            String savedName = reviewContentDTO.getReviewImage() + ext;
            // 파일 경로와 새로 저장할 파일 이름을 결합해서 최종 파일 경로를 설정
            String fullPath = filePath + "/" + savedName;

            try {
                // 파일 저장
                singleFile.transferTo(new File(fullPath));
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } else {
            // 이미지가 없을 경우 기본 값을 설정
            reviewContentDTO.setReviewImage(null);
        }

        storeService.createReview(reviewContentDTO);

        // 리뷰 달기 완성 후 유저 활동 +1 늘리기
        storeService.increaseReview(reviewContentDTO.getUserNo());
    }

    // 리뷰 삭제하기
    @DeleteMapping(value = "/{storeNo}/deletereview")
    public void deleteReview(@PathVariable("storeNo") long storeNo, @RequestParam("reviewNo") long reviewNo, @RequestParam("userNo") long userNo){
        System.out.println("리뷰 삭제 옴");
        System.out.println("zz");

        // 삭제할 리뷰 정보 가져오기(리뷰 이미지 포함)
        ReviewContentDTO reviewContentDTO = storeService.getReviewContent(reviewNo);

        // 리뷰 삭제
        storeService.deleteReview(reviewNo);

        // 리뷰 이미지 파일 삭제
        String pngFilePath = SHARED_FOLDER + "\\" + reviewContentDTO.getReviewImage() + ".png";
        String jpgFilePath = SHARED_FOLDER + "\\" + reviewContentDTO.getReviewImage() + ".jpg";

        File pngFile = new File(pngFilePath);
        File jpgFile = new File(jpgFilePath);

        if (reviewContentDTO.getReviewImage() != null) {
            if (pngFile.exists()) {
                if (pngFile.delete()) {
                    System.out.println("PNG 이미지 파일 삭제 성공: " + pngFilePath);
                } else {
                    System.out.println("PNG 이미지 파일 삭제 실패: " + pngFilePath);
                }
            } else if (jpgFile.exists()) {
                if (jpgFile.delete()) {
                    System.out.println("JPG 이미지 파일 삭제 성공: " + jpgFilePath);
                } else {
                    System.out.println("JPG 이미지 파일 삭제 실패: " + jpgFilePath);
                }
            } else {
                System.out.println("이미지 파일이 존재하지 않음: " + pngFilePath + " 또는 " + jpgFilePath);
            }
        } else {
            System.out.println("기본 이미지이므로 삭제하지 않습니다: " + reviewContentDTO.getReviewImage());
        }

        // 리뷰 달기 완성 후 유저 활동 - 1 감소
        storeService.decreaseReview(userNo);
    }

    // 리뷰 작성하기 버튼 활성화를 위한 리뷰 작성 권환 확인용 -> 예약 tbl에서 해당 아이디, 가게번호 넘겨서 확인하기
    @GetMapping(value = "/{storeNo}/getreviewlist")
    public ResponseEntity<List<ReservationInfoDTO>> getUserReviewList(@PathVariable("storeNo") long storeNum, @RequestParam("userId") String userId, @RequestParam("storeNo") long storeNo, Model model){
//        System.out.println("리뷰 권한 넘어옴");
//        System.out.println("userId : " + userId + " , storeNo : " + storeNo);

        List<ReservationInfoDTO> reservationList = new ArrayList<>();

        reservationList = storeService.getUserReviewList(userId, storeNo);

//        System.out.println("reservationList = " + reservationList);

        return ResponseEntity.ok(reservationList);

    }

    // 리뷰 버튼 활성화를 위한 리뷰 작성 권한 확인용 -> 리뷰 tbl에서 예약 번호 확인하기
    @GetMapping(value = "/{storeNo}/checkReviewList")
    public ResponseEntity<Boolean> checkReviewList(@PathVariable("storeNo") long storeNo, @RequestParam("resNo") long resNo){

//        System.out.println("리뷰 권한2 넘어옴");

        boolean result = storeService.checkReviewList(resNo);

        return ResponseEntity.ok(result);
    }

    // 예약 가능 인원 조회
    @GetMapping(value = "/{storeNo}/resPosNumber")
    public List<DayResPosNumDTO> getResPosNumber(@PathVariable("storeNo") long storeNum, @RequestParam("storeNo") long storeNo, @RequestParam("day") String day, @RequestParam("date") String date, @ModelAttribute StoreResPosNumDTO storeResPosNumDTO) {

        System.out.println("예약 가능 인원이요~~~");

        switch (day) {
            case "0" : storeResPosNumDTO.setrDay("TBL_SUNDAY"); break;
            case "1" : storeResPosNumDTO.setrDay("TBL_MONDAY"); break;
            case "2" : storeResPosNumDTO.setrDay("TBL_TUESDAY"); break;
            case "3" : storeResPosNumDTO.setrDay("TBL_WEDNESDAY"); break;
            case "4" : storeResPosNumDTO.setrDay("TBL_THURSDAY"); break;
            case "5" : storeResPosNumDTO.setrDay("TBL_FRIDAY"); break;
            case "6" : storeResPosNumDTO.setrDay("TBL_SATURDAY"); break;
        }

        storeResPosNumDTO.setStoreNo(storeNo);
        storeResPosNumDTO.setrDate(LocalDate.parse(date));

        System.out.println("예약 가능 ??? " + storeResPosNumDTO);

        List<DayResPosNumDTO> listDayResPosNum= storeService.getResPosNum(storeResPosNumDTO);

        System.out.println("listDayResPosNum = " + listDayResPosNum);

//        storeResPosNumDTO.setListDayResPosNumDTO(listDayResPosNum);
//        System.out.println("storeResPosNumDTO = " + storeResPosNumDTO);

        return listDayResPosNum;
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




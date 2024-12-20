package com.ohgiraffers.ukki.store.controller;

import com.ohgiraffers.ukki.store.model.dto.BannerDTO;
import com.ohgiraffers.ukki.store.model.dto.KeywordDTO;
import com.ohgiraffers.ukki.store.model.dto.OperationDTO;
import com.ohgiraffers.ukki.store.model.dto.StoreInfoDTO;
import com.ohgiraffers.ukki.store.model.service.StoreService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value="/")
public class StoreController {

    private final StoreService storeService;
    private final String SHARED_FOLDER = "\\\\I7E-74\\ukki_nas\\store";

    public StoreController(StoreService storeService){
        this.storeService = storeService;
    }

    // 검색 페이지 만들어지면 pathvariable로 변경하기
    @GetMapping(value="/store/test", produces = "application/json; charset=UTF-8")
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

    @GetMapping(value = "/storeProfile/5")
    public ResponseEntity<String> getProfileName(StoreInfoDTO storeInfoDTO){

        storeInfoDTO = storeService.getStoreInfo(storeInfoDTO);
        String profileName = storeInfoDTO.getStoreProfile();
        System.out.println(profileName);
        return ResponseEntity.ok(profileName);
    }

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

//    매뉴 이미지 불러오기 로직 만들 예정
    @GetMapping(value = "/storeMenu/5")
    public ResponseEntity<String> getMenuName(StoreInfoDTO storeInfoDTO){

        storeInfoDTO = storeService.getStoreInfo(storeInfoDTO);
        String menuName = storeInfoDTO.getStoreMenu();

        return ResponseEntity.ok(menuName);
    }

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

}


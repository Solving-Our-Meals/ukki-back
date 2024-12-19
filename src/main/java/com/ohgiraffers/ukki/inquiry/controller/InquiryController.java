package com.ohgiraffers.ukki.inquiry.controller;

import com.ohgiraffers.ukki.common.InquiryState;
import com.ohgiraffers.ukki.inquiry.model.dto.InquiryCategoryDTO;
import com.ohgiraffers.ukki.inquiry.model.dto.InquiryDTO;
import com.ohgiraffers.ukki.inquiry.model.dto.InquiryListDTO;
import com.ohgiraffers.ukki.inquiry.model.service.InquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ohgiraffers.ukki.common.InquiryState.PROCESSING;

@RestController
@RequestMapping("/inquiries")
public class InquiryController {

    private final InquiryService inquiryService;

    @Autowired
    public InquiryController(InquiryService inquiryService){
        this.inquiryService = inquiryService;
    }

    @PostMapping(value = "/users")
    public ResponseEntity<?> addUserInquiry(@RequestPart(value = "data") InquiryDTO inquiryDTO,
                                            @RequestParam(value = "file", required = false)MultipartFile file){
//        지금 예약 DTO를 따로 만들어서 진행하고 있다. 여기서 userNo는 보안에서 가져올 것인지 아니면 웹페이지에서 받을 것인지 정해야한다.


        if (file != null && !file.isEmpty()) {
            //      마지막 문의번호 불러오기
            Integer lastNo = inquiryService.lastInquiryNo();
            if(lastNo==null){
                lastNo = 1;
                inquiryDTO.setFile("inquiryFile"+1);
            }else{
                inquiryDTO.setFile("inquiryFile"+(lastNo+1));
            }

            //                파일이름에서 확장자 뽑아내기
            String fileExtension = "";
            String originalFileName =file.getOriginalFilename();
            int dotIndex = originalFileName.lastIndexOf('.');
            if (dotIndex > 0 && dotIndex < originalFileName.length() - 1){
                fileExtension = originalFileName.substring(dotIndex);
            }
            //                최종파일 이름에 확장자 추가
            String fileName = "inquiryFile"+(lastNo+1)+fileExtension;
            try{
//                경로설정
                Path networkPath = Paths.get("\\\\192.168.0.138\\ukki_nas\\inquiry");
                if(!Files.exists(networkPath)){
                    Files.createDirectories(networkPath);
                }

//                파일 저장 - 경로에 파일이름 붙이기
//                StandardCopyOption.REPLACE_EXISTING은 대상 경로에 동일한 이름의 파일이 이미 존재할 경우 그 카일을 덮어쓰도록하는 옵션
                Path filePath = networkPath.resolve(fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                System.out.println(file.getInputStream());
                System.out.println("파일 저장 성공: " + fileName);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) .body("파일 저장 중 오류가 발생했습니다.");
            }
        }

        inquiryDTO.setInquiryDate(LocalDate.now());
        inquiryDTO.setState(PROCESSING);


        int result = inquiryService.addInquiry(inquiryDTO);
        String message = "";
        if(result>0){
            message="문의가 성공적으로 전달되었습니다.";
        }else {
            message="문의에 실패했습니다.";
        }

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("message", message);

        System.out.println(message);
        return ResponseEntity.ok(responseMap);
    }

    @PostMapping(value = "/stores")
    public ResponseEntity<?> addStoreInquiry(@RequestPart(value = "data") InquiryDTO inquiryDTO,
                                             @RequestParam(value = "file", required = false)MultipartFile file){
        if (file != null && !file.isEmpty()) {
            //      마지막 문의번호 불러오기
            Integer lastNo = inquiryService.lastInquiryNo();
            if(lastNo==null){
                lastNo = 1;
                inquiryDTO.setFile("inquiryFile"+1);
            }else{
                inquiryDTO.setFile("inquiryFile"+(lastNo+1));
            }

            //                파일이름에서 확장자 뽑아내기
            String fileExtension = "";
            String originalFileName =file.getOriginalFilename();
            int dotIndex = originalFileName.lastIndexOf('.');
            if (dotIndex > 0 && dotIndex < originalFileName.length() - 1){
                fileExtension = originalFileName.substring(dotIndex);
            }
            //                최종파일 이름에 확장자 추가
            String fileName = "inquiryFile"+(lastNo+1)+fileExtension;
            try{
//                경로설정
                Path networkPath = Paths.get("\\\\192.168.0.138\\ukki_nas\\inquiry");
                if(!Files.exists(networkPath)){
                    Files.createDirectories(networkPath);
                }

//                파일 저장 - 경로에 파일이름 붙이기
//                StandardCopyOption.REPLACE_EXISTING은 대상 경로에 동일한 이름의 파일이 이미 존재할 경우 그 카일을 덮어쓰도록하는 옵션
                Path filePath = networkPath.resolve(fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                System.out.println(file.getInputStream());
                System.out.println("파일 저장 성공: " + fileName);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) .body("파일 저장 중 오류가 발생했습니다.");
            }
        }

        inquiryDTO.setInquiryDate(LocalDate.now());
        inquiryDTO.setState(PROCESSING);

        int result = inquiryService.addInquiry(inquiryDTO);
        String message = "";
        if(result>0){
            message="문의가 성공적으로 전달되었습니다.";
        }else {
            message="문의에 실패했습니다.";
        }

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("message", message);

        System.out.println(message);
        return ResponseEntity.ok(responseMap);
    }

    @GetMapping(value = "/categories", produces = "application/json; charset=UTF-8")
    public ResponseEntity<?> findUserInquiryCategory(){
        List<InquiryCategoryDTO> category = inquiryService.findCategory();

        return ResponseEntity.ok(category);
    }

    @GetMapping(value = "/inquirylist")
    public  ResponseEntity<?> findInquiryList(){
//        userNo 받아와야한다. auth쪽에서 해야할 듯 일단 num는 3번으로 진행하자
        List<InquiryDTO> inquiryList = inquiryService.findInquiryList(3);
        List<InquiryListDTO> necessaryInquiryList = new ArrayList<>();

        for(int i=0; i<inquiryList.size(); i++){
            InquiryListDTO temp = new InquiryListDTO();
            temp.setInquiryTitle(inquiryList.get(i).getInquiryTitle());
            temp.setInquiryDate(inquiryList.get(i).getInquiryDate());
            temp.setState(inquiryList.get(i).getState().getInquiryState());
            necessaryInquiryList.add(temp);
        }

        return ResponseEntity.ok(necessaryInquiryList);
    }
}

package com.ohgiraffers.ukki.inquiry.controller;

import com.ohgiraffers.ukki.inquiry.model.dto.InquiryCategoryDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import com.ohgiraffers.ukki.inquiry.model.dto.InquiryDTO;
import com.ohgiraffers.ukki.inquiry.model.service.InquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(value = "/users", produces = "application/json; charset=UTF-8")
    public ResponseEntity<?> addUserInquiry(@RequestBody InquiryDTO inquiryDTO){
//        지금 예약 DTO를 따로 만들어서 진행하고 있다. 여기서 userNo는 보안에서 가져올 것인지 아니면 웹페이지에서 받을 것인지 정해야한다.
//        현재 파일은 null상태이다
        
        System.out.println("hi");
        LocalDate now = LocalDate.now();
        String inquiryFileName = ""+now+inquiryDTO.getUserNo()+"inquiryFile";

        inquiryDTO.setInquiryDate(now);
        inquiryDTO.setState(PROCESSING);
        inquiryDTO.setFile(inquiryFileName);

        int result = inquiryService.addInquiry(inquiryDTO);
        String message = "";
        if(result>0){
            message="문의가 성공적으로 전달되었습니다.";
        }else {
            message="문의에 실패했습니다.";
        }

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("message", message);

        return ResponseEntity.ok(responseMap);
    }

    @PostMapping(value = "/stores", produces = "application/json; charset=UTF-8")
    public ResponseEntity<?> addStoreInquiry(@RequestBody InquiryDTO inquiryDTO){
        System.out.println("hi");
        LocalDate now = LocalDate.now();
        String inquiryFileName = ""+now+inquiryDTO.getUserNo()+"inquiryFile";

        inquiryDTO.setInquiryDate(now);
        inquiryDTO.setState(PROCESSING);
        inquiryDTO.setFile(inquiryFileName);

        int result = inquiryService.addInquiry(inquiryDTO);
        String message = "";
        if(result>0){
            message="문의가 성공적으로 전달되었습니다.";
        }else {
            message="문의에 실패했습니다.";
        }

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("message", message);

        return ResponseEntity.ok(responseMap);
    }

    @GetMapping(value = "/users", produces = "application/json; charset=UTF-8")
    public ResponseEntity<?> findUserInquiryCategory(){
        List<InquiryCategoryDTO> category = inquiryService.findCategory();
        List<InquiryCategoryDTO> userCategory = new ArrayList<>();

        System.out.println("카테고리 하이");
        for(int i = 0; i<4; i++) {
            userCategory.add(category.get(i));
        }

        return ResponseEntity.ok(userCategory);
    }
}

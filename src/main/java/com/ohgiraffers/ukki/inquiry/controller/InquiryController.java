package com.ohgiraffers.ukki.inquiry.controller;

import com.ohgiraffers.ukki.inquiry.model.dto.InquiryCategoryDTO;
import com.ohgiraffers.ukki.inquiry.model.dto.InquiryDTO;
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
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;

import com.ohgiraffers.ukki.common.service.GoogleDriveService;
import static com.ohgiraffers.ukki.common.InquiryState.PROCESSING;

@RestController
@RequestMapping("/inquiries")
public class InquiryController {

//    private final String SHARED_FOLDER = "\\\\192.168.0.138\\ukki_nas\\inquiry";
private final String SHARED_FOLDER = "C:\\Users\\admin\\Desktop\\ukkiImg";
    private final InquiryService inquiryService;
    private final GoogleDriveService googleDriveService;
    private static final String INQUIRY_FOLDER_ID = "1Bzigy3LlWfu5wAj7vB5Xdp_QapW76eQG";


    @Autowired
    public InquiryController(InquiryService inquiryService, GoogleDriveService googleDriveService){
        this.inquiryService = inquiryService;
        this.googleDriveService = googleDriveService;
    }
    @PostMapping(value = "/users")
    public ResponseEntity<?> addUserInquiry(@RequestPart(value = "data") InquiryDTO inquiryDTO,
                                            @RequestParam(value = "file", required = false)MultipartFile file){
//        지금 예약 DTO를 따로 만들어서 진행하고 있다. 여기서 userNo는 보안에서 가져올 것인지 아니면 웹페이지에서 받을 것인지 정해야한다.
        if (file != null && !file.isEmpty()) {
            try {
                Integer lastNo = inquiryService.lastInquiryNo();
                String fileName = "inquiry" + (lastNo == null ? 1 : lastNo + 1);

                // Google Drive에 파일 업로드 (파일명 지정)
                String fileId = googleDriveService.uploadFile(file, INQUIRY_FOLDER_ID, fileName);
                String fileUrl = googleDriveService.getFileUrl(fileId);
                System.out.println(fileUrl);
                inquiryDTO.setFile(fileUrl);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("파일 업로드 중 오류가 발생했습니다: " + e.getMessage());
            }
        };

        inquiryDTO.setInquiryDate(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        inquiryDTO.setState(PROCESSING);
        System.out.println(inquiryDTO);

        Map<String, Object> responseMap = new HashMap<>();
        int result = inquiryService.addInquiry(inquiryDTO);

        if (result > 0) {
            responseMap.put("message", "문의가 성공적으로 전달되었습니다.");
            return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(responseMap);
        } else {
            responseMap.put("message", "문의에 실패했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body(responseMap);
        }
    }

    @GetMapping(value = "/categories", produces = "application/json; charset=UTF-8")
    public ResponseEntity<?> findUserInquiryCategory(){
        List<InquiryCategoryDTO> category = inquiryService.findCategory();

        return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(category);
    }

    public String fileController(MultipartFile file, String fileName){

        //                파일이름에서 확장자 뽑아내기
        String fileExtension = "";
        String originalFileName =file.getOriginalFilename();
        int dotIndex = originalFileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < originalFileName.length() - 1){
            fileExtension = originalFileName.substring(dotIndex);
        }
        //                최종파일 이름에 확장자 추가
        String fileSetName = fileName+fileExtension;
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
            return fileSetName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}


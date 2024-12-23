package com.ohgiraffers.ukki.report.controller;

import com.ohgiraffers.ukki.report.model.dto.ReportDTO;
import com.ohgiraffers.ukki.report.model.dto.ReportListDTO;
import com.ohgiraffers.ukki.report.model.service.ReportService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService){
        this.reportService = reportService;
    }

    @GetMapping("/list")
    public ResponseEntity<?> findReportList(){

        List<ReportDTO> reportList = reportService.findReportList(29);
        List<ReportListDTO> necessaryReportList = new ArrayList<>();

        for(int i=0; i<reportList.size(); i++){
            ReportListDTO temp = new ReportListDTO();
            temp.setDivision("report");
            temp.setReportNo(reportList.get(i).getReportNo());
            temp.setReportTitle(reportList.get(i).getReportTitle());
            temp.setReportDate(reportList.get(i).getReportDate());
            temp.setState(reportList.get(i).getState().getInquiryState());
            temp.setState(reportList.get(i).getState().getInquiryState());
            necessaryReportList.add(temp);
        }

        return ResponseEntity.ok(necessaryReportList);
    }

    @GetMapping("/list/{reportNo}")
    public ResponseEntity<?> reportInfo(@PathVariable int reportNo){
        ReportDTO reportDTO = reportService.reportInfo(reportNo);

        return ResponseEntity.ok(reportDTO);
    }

    @DeleteMapping("list/{reportNo}")
    public ResponseEntity<?> reportDelete(@PathVariable int reportNo){
        int result = reportService.reportDelete(reportNo);
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
    @PutMapping(value = "/list/{reportNo}")
    public ResponseEntity<?> reportUpdate(@PathVariable int reportNo,
                                           @RequestPart(value = "data") ReportDTO reportDTO){

        System.out.println("왔당");
        System.out.println(reportDTO);
        reportDTO.setReportNo(reportNo);

        int result = reportService.reportUpdate(reportDTO);

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
    public int fileController(MultipartFile file, String fileName){

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
            Path networkPath = Paths.get("\\\\192.168.0.138\\ukki_nas\\inquiry");
            if(!Files.exists(networkPath)){
                Files.createDirectories(networkPath);
            }

//                파일 저장 - 경로에 파일이름 붙이기
//                StandardCopyOption.REPLACE_EXISTING은 대상 경로에 동일한 이름의 파일이 이미 존재할 경우 그 카일을 덮어쓰도록하는 옵션
            Path filePath = networkPath.resolve(fileSetName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            System.out.println(file.getInputStream());
            System.out.println("파일 저장 성공: " + fileSetName);
            return 2;
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
    }
}

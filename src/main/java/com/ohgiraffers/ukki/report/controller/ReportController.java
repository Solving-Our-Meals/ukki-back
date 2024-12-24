package com.ohgiraffers.ukki.report.controller;

import com.ohgiraffers.ukki.report.model.dto.ReportDTO;
import com.ohgiraffers.ukki.report.model.dto.ReportListDTO;
import com.ohgiraffers.ukki.report.model.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}

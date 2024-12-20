package com.ohgiraffers.ukki.report.controller;

import com.ohgiraffers.ukki.report.model.dto.ReportDTO;
import com.ohgiraffers.ukki.report.model.dto.ReportListDTO;
import com.ohgiraffers.ukki.report.model.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/report")
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

}

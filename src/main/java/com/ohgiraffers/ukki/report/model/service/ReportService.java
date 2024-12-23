package com.ohgiraffers.ukki.report.model.service;

import com.ohgiraffers.ukki.report.model.dao.ReportMapper;
import com.ohgiraffers.ukki.report.model.dto.ReportDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {
    private final ReportMapper reportMapper;

    @Autowired
    public ReportService(ReportMapper reportMapper){
        this.reportMapper = reportMapper;
    }

    public List<ReportDTO> findReportList(int userNo) {
        return reportMapper.findReportList(userNo);
    }

    public ReportDTO reportInfo(int reportNo) {
        return reportMapper.reportInfo(reportNo);
    }

    public int reportDelete(int reportNo) {
        return reportMapper.reportDelete(reportNo);
    }

    public int reportUpdate(ReportDTO reportDTO) {
        return reportMapper.reportUpdate(reportDTO);
    }
}

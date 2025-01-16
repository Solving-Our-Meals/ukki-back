package com.ohgiraffers.ukki.admin.inquiry.model.service;

import com.ohgiraffers.ukki.admin.inquiry.model.dao.AdminInquiryMapper;
import com.ohgiraffers.ukki.admin.inquiry.model.dto.AnswerDTO;
import com.ohgiraffers.ukki.admin.inquiry.model.dto.InquiryInfoDTO;
import com.ohgiraffers.ukki.admin.inquiry.model.dto.InquiryListDTO;
import com.ohgiraffers.ukki.admin.inquiry.model.dto.ReportInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminInquiryService {
    private final AdminInquiryMapper adminInquiryMapper;

    @Autowired
    public AdminInquiryService(AdminInquiryMapper adminInquiryMapper){
        this.adminInquiryMapper = adminInquiryMapper;
    }

    public int processingInquiry() {
        return adminInquiryMapper.processingInquiry();
    }

    public List<InquiryListDTO> searchUserInquiry(String category, String word) {
        return adminInquiryMapper.searchUserInquiry(category, word);
    }

    public int totalInquiry() {
        return adminInquiryMapper.totalInquiry();
    }

    public List<InquiryListDTO> searchStoreInquiry(String category, String word) {
        return adminInquiryMapper.searchStoreInquiry(category, word);
    }

    public List<InquiryListDTO> searchStoreReportInquiry(String category, String word) {
        return adminInquiryMapper.searchStoreReportInquiry(category, word);
    }

    public InquiryInfoDTO inquiryInfo(int inquiryNo) {
        return adminInquiryMapper.inquiryInfo(inquiryNo);
    }

    public ReportInfoDTO reportInfo(int reportNo) {
        return adminInquiryMapper.reportInfo(reportNo);
    }

    public void inquiryAnswer(AnswerDTO answer) {
        adminInquiryMapper.inquiryAnswer(answer);
    }

    public void inquiryDelete(int inquiryNo) {
        adminInquiryMapper.inquiryDelete(inquiryNo);
    }

    public void reportAnswer(AnswerDTO answer) {
        adminInquiryMapper.reportAnswer(answer);
    }

    public void reportDelete(int reportNo) {
        adminInquiryMapper.reportDelete(reportNo);
    }
}

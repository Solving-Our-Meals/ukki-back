package com.ohgiraffers.ukki.admin.inquiry.model.service;

import com.ohgiraffers.ukki.admin.inquiry.model.dao.AdminInquiryMapper;
import com.ohgiraffers.ukki.admin.inquiry.model.dto.AnswerDTO;
import com.ohgiraffers.ukki.admin.inquiry.model.dto.AdminInquiryInfoDTO;
import com.ohgiraffers.ukki.admin.inquiry.model.dto.AdminInquiryListDTO;
import com.ohgiraffers.ukki.admin.inquiry.model.dto.AdminReportInfoDTO;
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

    public List<AdminInquiryListDTO> searchUserInquiry(String category, String word) {
        return adminInquiryMapper.searchUserInquiry(category, word);
    }

    public int totalInquiry() {
        return adminInquiryMapper.totalInquiry();
    }

    public List<AdminInquiryListDTO> searchStoreInquiry(String category, String word) {
        return adminInquiryMapper.searchStoreInquiry(category, word);
    }

    public List<AdminInquiryListDTO> searchStoreReportInquiry(String category, String word) {
        return adminInquiryMapper.searchStoreReportInquiry(category, word);
    }

    public AdminInquiryInfoDTO inquiryInfo(int inquiryNo) {
        return adminInquiryMapper.inquiryInfo(inquiryNo);
    }

    public AdminReportInfoDTO reportInfo(int reportNo) {
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

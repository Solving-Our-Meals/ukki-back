package com.ohgiraffers.ukki.admin.inquiry.model.service;

import com.ohgiraffers.ukki.admin.inquiry.model.dao.AdminInquiryMapper;
import com.ohgiraffers.ukki.admin.inquiry.model.dto.InquiryListDTO;
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
}

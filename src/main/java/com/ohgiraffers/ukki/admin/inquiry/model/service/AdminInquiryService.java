package com.ohgiraffers.ukki.admin.inquiry.model.service;

import com.ohgiraffers.ukki.admin.inquiry.model.dao.AdminInquiryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}

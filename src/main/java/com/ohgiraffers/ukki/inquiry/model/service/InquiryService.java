package com.ohgiraffers.ukki.inquiry.model.service;

import com.ohgiraffers.ukki.inquiry.model.dao.InquiryMapper;
import com.ohgiraffers.ukki.inquiry.model.dto.InquiryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InquiryService {
    private final InquiryMapper inquiryMapper;

    @Autowired
    public InquiryService(InquiryMapper inquiryMapper){
        this.inquiryMapper = inquiryMapper;
    }


    public int addInquiry(InquiryDTO inquiryDTO) {
        return inquiryMapper.addInquiry(inquiryDTO);
    }
}
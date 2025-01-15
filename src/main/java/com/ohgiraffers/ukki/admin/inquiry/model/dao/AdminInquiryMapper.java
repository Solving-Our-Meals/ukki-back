package com.ohgiraffers.ukki.admin.inquiry.model.dao;

import com.ohgiraffers.ukki.admin.inquiry.model.dto.InquiryListDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminInquiryMapper {
    int processingInquiry();

    List<InquiryListDTO> searchUserInquiry(String category, String word);

    int totalInquiry();

    List<InquiryListDTO> searchStoreInquiry(String category, String word);

    List<InquiryListDTO> searchStoreReportInquiry(String category, String word);
}

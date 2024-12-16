package com.ohgiraffers.ukki.inquiry.model.dao;

import com.ohgiraffers.ukki.inquiry.model.dto.InquiryDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InquiryMapper {
    int addInquiry(InquiryDTO inquiryDTO);
}

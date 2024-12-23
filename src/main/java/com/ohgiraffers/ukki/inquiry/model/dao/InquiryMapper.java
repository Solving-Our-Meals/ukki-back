package com.ohgiraffers.ukki.inquiry.model.dao;

import com.ohgiraffers.ukki.inquiry.model.dto.InquiryCategoryDTO;
import com.ohgiraffers.ukki.inquiry.model.dto.InquiryDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InquiryMapper {
    int addInquiry(InquiryDTO inquiryDTO);

    List<InquiryCategoryDTO> findCategory();

    Integer lastInquiryNo();

    List<InquiryDTO> findInquiryList(int userNo);

    InquiryDTO inquiryInfo(int inquiryNo);

    int inquiryUpdate(InquiryDTO inquiryDTO);

    int inquiryDelete(int inquiryNo);
}

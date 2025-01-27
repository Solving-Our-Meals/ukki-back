package com.ohgiraffers.ukki.admin.inquiry.model.dao;

import com.ohgiraffers.ukki.admin.inquiry.model.dto.AnswerDTO;
import com.ohgiraffers.ukki.admin.inquiry.model.dto.AdminInquiryInfoDTO;
import com.ohgiraffers.ukki.admin.inquiry.model.dto.AdminInquiryListDTO;
import com.ohgiraffers.ukki.admin.inquiry.model.dto.AdminReportInfoDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminInquiryMapper {
    int processingInquiry();

    List<AdminInquiryListDTO> searchUserInquiry(String category, String word);

    int totalInquiry();

    List<AdminInquiryListDTO> searchStoreInquiry(String category, String word);

    List<AdminInquiryListDTO> searchStoreReportInquiry(String category, String word);

    AdminInquiryInfoDTO inquiryInfo(int inquiryNo);

    AdminReportInfoDTO reportInfo(int reportNo);

    void inquiryAnswer(AnswerDTO answer);

    void inquiryDelete(int inquiryNo);

    void reportAnswer(AnswerDTO answer);

    void reportDelete(int reportNo);
}

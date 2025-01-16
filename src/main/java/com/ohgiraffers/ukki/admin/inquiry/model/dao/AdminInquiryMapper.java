package com.ohgiraffers.ukki.admin.inquiry.model.dao;

import com.ohgiraffers.ukki.admin.inquiry.model.dto.AnswerDTO;
import com.ohgiraffers.ukki.admin.inquiry.model.dto.InquiryInfoDTO;
import com.ohgiraffers.ukki.admin.inquiry.model.dto.InquiryListDTO;
import com.ohgiraffers.ukki.admin.inquiry.model.dto.ReportInfoDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminInquiryMapper {
    int processingInquiry();

    List<InquiryListDTO> searchUserInquiry(String category, String word);

    int totalInquiry();

    List<InquiryListDTO> searchStoreInquiry(String category, String word);

    List<InquiryListDTO> searchStoreReportInquiry(String category, String word);

    InquiryInfoDTO inquiryInfo(int inquiryNo);

    ReportInfoDTO reportInfo(int reportNo);

    void inquiryAnswer(AnswerDTO answer);

    void inquiryDelete(int inquiryNo);

    void reportAnswer(AnswerDTO answer);

    void reportDelete(int reportNo);
}

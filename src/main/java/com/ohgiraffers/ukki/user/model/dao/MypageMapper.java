package com.ohgiraffers.ukki.user.model.dao;

import com.ohgiraffers.ukki.common.InquiryState;
import com.ohgiraffers.ukki.user.model.dto.MypageDTO;
import com.ohgiraffers.ukki.user.model.dto.MypageInquiryDTO;
import com.ohgiraffers.ukki.user.model.dto.MypageReservationDTO;
import com.ohgiraffers.ukki.user.model.dto.MypageReviewDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MypageMapper {
    MypageDTO findUserInfoByUserId(String userId);

    List<MypageReservationDTO> findUserReservationByUserId(String userId);

    List<MypageReviewDTO> findUserReviewByUserId(String userId);

    int deleteReviewById(int reviewNo);

    List<MypageInquiryDTO> findUserInquiryByUserId(String userId);

    int updateInquiryStatus(int inquiryNo, InquiryState inquiryState);
}

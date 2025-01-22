package com.ohgiraffers.ukki.user.model.dao;

import com.ohgiraffers.ukki.common.InquiryState;
import com.ohgiraffers.ukki.user.model.dto.*;
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

    int updateInquiry(MypageInquiryDTO inquiryToUpdate);

    int deleteInquiryById(int inquiryNo);

    void saveFile(int inquiryNo, String fileName, String string);

    String findPasswordByUserId(String userId);

    MypageInfoDTO findUserProfileInfo(String userId);

    int updateUserInfo(MypageUpdateUserInfoDTO updateUserInfoDTO);

    MypageReviewDTO findUserReviewDetailByReviewNo(Long reviewNo);

    int updateUserProfileImage(MypageProfileImageDTO mypageProfileImageDTO);

    String findProfileImagePathByUserId(String userId);

    int deleteUserById(String userId);

    MypageReservationDetailDTO findReservationDetailByResNo(int resNo);

    Long getUserNoById(String userId);

    List<String> getReviewImagesByUserId(Long userNo);

    int deleteReviewsByUserId(Long userNo);

    int getNoshowCountByUserNo(Long userNo);

    String getEmailByUserNo(Long userNo);

    MypageDeleteAccount getUserNoByIdForNoshow(String userId);

    int insertEmailIntoNoshow(String email, int noshow);

    List<MypageReservationDTO> findUserReservationByUserIdWithSearch(String userId, String search);

    List<MypageReviewDTO> findUserReviewByUserIdWithSearch(String userId, String search);

    List<MypageInquiryDTO> findUserInquiryByUserIdWithSearch(String userId, String search);

    MypageInquiryDTO findInquiryById(int inquiryNo);

    int deleteUserAct(Long userNo);

    int deleteReservation(Long resNo);

    MypageReservationQRDTO findReservationQRById(Long resNo);

    int countReservation(Long userNo);
}

package com.ohgiraffers.ukki.admin.review.model.dao;

import com.ohgiraffers.ukki.admin.review.model.dto.AdminReviewInfoDTO;
import com.ohgiraffers.ukki.admin.review.model.dto.AdminReviewListDTO;
import com.ohgiraffers.ukki.admin.user.model.dto.AdminUserReviewDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminReviewMapper {
    List<AdminUserReviewDTO> userReviewList();

    int totalReview();

    List<AdminReviewListDTO> searchReview(Map<String, String> params);

    AdminReviewInfoDTO searchReviewInfo(String reviewNo);

    int deleteReview(int reviewNo);

    void decreaseReviewCount(int userNo);
}

package com.ohgiraffers.ukki.admin.review.model.dao;

import com.ohgiraffers.ukki.admin.review.model.dto.ReviewListDTO;
import com.ohgiraffers.ukki.admin.user.model.dto.AdminUserReviewDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminReviewMapper {
    List<AdminUserReviewDTO> userReviewList();

    int totalReview();

    List<ReviewListDTO> searchReview(Map<String, String> params);
}

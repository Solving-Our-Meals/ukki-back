package com.ohgiraffers.ukki.admin.review.model.dao;

import com.ohgiraffers.ukki.admin.user.model.dto.AdminUserReviewDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminReviewMapper {
    List<AdminUserReviewDTO> userReviewList();
}

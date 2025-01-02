package com.ohgiraffers.ukki.admin.review.model.service;

import com.ohgiraffers.ukki.admin.review.model.dao.AdminReviewMapper;
import com.ohgiraffers.ukki.admin.user.model.dto.AdminUserReviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminReviewService {

    private final AdminReviewMapper adminReviewMapper;

    @Autowired
    public AdminReviewService(AdminReviewMapper adminReviewMapper) {
        this.adminReviewMapper = adminReviewMapper;
    }

    public List<AdminUserReviewDTO> userReviewList() {
        return adminReviewMapper.userReviewList();
    }
}

package com.ohgiraffers.ukki.admin.review.model.service;

import com.ohgiraffers.ukki.admin.review.model.dao.AdminReviewMapper;
import com.ohgiraffers.ukki.admin.review.model.dto.AdminReviewInfoDTO;
import com.ohgiraffers.ukki.admin.review.model.dto.AdminReviewListDTO;
import com.ohgiraffers.ukki.admin.user.model.dto.AdminUserReviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public int totalReview() {
        return adminReviewMapper.totalReview();
    }

    public List<AdminReviewListDTO> searchReview(String category, String word) {
        Map<String, String> params = new HashMap<>();
        params.put("category", category);
        params.put("word", word);
        return adminReviewMapper.searchReview(params);
    }

    public AdminReviewInfoDTO searchReviewInfo(String reviewNo) {
        return adminReviewMapper.searchReviewInfo(reviewNo);
    }

    public int deleteReview(int reviewNo) {
        return adminReviewMapper.deleteReview(reviewNo);

    }

    public void decreaseReviewCount(int userNo) {
        adminReviewMapper.decreaseReviewCount(userNo);
    }
}

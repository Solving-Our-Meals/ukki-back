package com.ohgiraffers.ukki.admin.user.model.dto;

public class AdminUserReviewDTO {
    private int userNo;
    private int totalReview;

    public AdminUserReviewDTO() {}

    public AdminUserReviewDTO(int userNo, int totalReview) {
        this.userNo = userNo;
        this.totalReview = totalReview;
    }

    public int getUserNo() {
        return userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }

    public int getTotalReview() {
        return totalReview;
    }

    public void setTotalReview(int totalReview) {
        this.totalReview = totalReview;
    }

    @Override
    public String toString() {
        return "AdminUserReviewDTO{" +
                "userNo=" + userNo +
                ", totalReview=" + totalReview +
                '}';
    }
}

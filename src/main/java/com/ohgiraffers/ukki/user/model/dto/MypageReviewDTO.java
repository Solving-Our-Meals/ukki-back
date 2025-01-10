package com.ohgiraffers.ukki.user.model.dto;

import java.util.Date;

public class MypageReviewDTO {
    private String userId;
    private String storeName;
    private Date reviewDate;
    private String reviewText;
    private String star;

    public MypageReviewDTO() {}

    public MypageReviewDTO(String userId, String storeName, Date reviewDate, String reviewText, String star) {
        this.userId = userId;
        this.storeName = storeName;
        this.reviewDate = reviewDate;
        this.reviewText = reviewText;
        this.star = star;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    @Override
    public String toString() {
        return "MypageReviewDTO{" +
                "userId='" + userId + '\'' +
                ", storeName='" + storeName + '\'' +
                ", reviewDate=" + reviewDate +
                ", reviewText='" + reviewText + '\'' +
                ", star='" + star + '\'' +
                '}';
    }
}

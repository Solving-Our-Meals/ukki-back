package com.ohgiraffers.ukki.user.model.dto;

import java.util.Date;

public class MypageReviewDTO {
    private String userProfile;
    private String userId;
    private int userNo;
    private int storeNo;
    private int reviewNo;
    private String storeName;
    private Date reviewDate;
    private String reviewText;
    private String star;

    public MypageReviewDTO() {}

    public MypageReviewDTO(String userProfile, String userId, int userNo, int storeNo, int reviewNo, String storeName, Date reviewDate, String reviewText, String star) {
        this.userProfile = userProfile;
        this.userId = userId;
        this.userNo = userNo;
        this.storeNo = storeNo;
        this.reviewNo = reviewNo;
        this.storeName = storeName;
        this.reviewDate = reviewDate;
        this.reviewText = reviewText;
        this.star = star;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getUserNo() {
        return userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }

    public int getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(int storeNo) {
        this.storeNo = storeNo;
    }

    public int getReviewNo() {
        return reviewNo;
    }

    public void setReviewNo(int reviewNo) {
        this.reviewNo = reviewNo;
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
                "userProfile='" + userProfile + '\'' +
                ", userId='" + userId + '\'' +
                ", userNo=" + userNo +
                ", storeNo=" + storeNo +
                ", reviewNo=" + reviewNo +
                ", storeName='" + storeName + '\'' +
                ", reviewDate=" + reviewDate +
                ", reviewText='" + reviewText + '\'' +
                ", star='" + star + '\'' +
                '}';
    }
}

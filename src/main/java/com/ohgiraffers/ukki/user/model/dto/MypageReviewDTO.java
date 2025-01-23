package com.ohgiraffers.ukki.user.model.dto;

import java.util.Date;

public class MypageReviewDTO {
    private String userProfile;
    private String userId;
    private String userName;
    private int userNo;
    private int storeNo;
    private String storeName;
    private int reviewNo;
    private String reviewDate;
    private String reviewPicture;
    private String reviewText;
    private String star;
    private String search;

    public MypageReviewDTO() {}

    public MypageReviewDTO(String userProfile, String userId, String userName, int userNo, int storeNo, String storeName, int reviewNo, String reviewDate, String reviewPicture, String reviewText, String star, String search) {
        this.userProfile = userProfile;
        this.userId = userId;
        this.userName = userName;
        this.userNo = userNo;
        this.storeNo = storeNo;
        this.storeName = storeName;
        this.reviewNo = reviewNo;
        this.reviewDate = reviewDate;
        this.reviewPicture = reviewPicture;
        this.reviewText = reviewText;
        this.star = star;
        this.search = search;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getReviewNo() {
        return reviewNo;
    }

    public void setReviewNo(int reviewNo) {
        this.reviewNo = reviewNo;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getReviewPicture() {
        return reviewPicture;
    }

    public void setReviewPicture(String reviewPicture) {
        this.reviewPicture = reviewPicture;
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

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    @Override
    public String toString() {
        return "MypageReviewDTO{" +
                "userProfile='" + userProfile + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userNo=" + userNo +
                ", storeNo=" + storeNo +
                ", storeName='" + storeName + '\'' +
                ", reviewNo=" + reviewNo +
                ", reviewDate='" + reviewDate + '\'' +
                ", reviewPicture='" + reviewPicture + '\'' +
                ", reviewText='" + reviewText + '\'' +
                ", star='" + star + '\'' +
                ", search='" + search + '\'' +
                '}';
    }
}

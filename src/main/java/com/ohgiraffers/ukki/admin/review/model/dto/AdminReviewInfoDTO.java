package com.ohgiraffers.ukki.admin.review.model.dto;

public class AdminReviewInfoDTO {

    private int reviewNo;
    private String reviewContent;
    private int reviewScope;
    private String reviewDate;
    private String reviewImage;
    private int reportCount;
    private int storeNo;
    private String storeName;
    private int userNo;
    private String userName;
    private String userProfile;
    private String userId;

    public AdminReviewInfoDTO() {}

    public AdminReviewInfoDTO(int reviewNo, String reviewContent, int reviewScope, String reviewDate, String reviewImage, int reportCount, int storeNo, String storeName, int userNo, String userName, String userProfile, String userId) {
        this.reviewNo = reviewNo;
        this.reviewContent = reviewContent;
        this.reviewScope = reviewScope;
        this.reviewDate = reviewDate;
        this.reviewImage = reviewImage;
        this.reportCount = reportCount;
        this.storeNo = storeNo;
        this.storeName = storeName;
        this.userNo = userNo;
        this.userName = userName;
        this.userProfile = userProfile;
        this.userId = userId;
    }

    public int getReviewNo() {
        return reviewNo;
    }

    public void setReviewNo(int reviewNo) {
        this.reviewNo = reviewNo;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public int getReviewScope() {
        return reviewScope;
    }

    public void setReviewScope(int reviewScope) {
        this.reviewScope = reviewScope;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getReviewImage() {
        return reviewImage;
    }

    public void setReviewImage(String reviewImage) {
        this.reviewImage = reviewImage;
    }

    public int getReportCount() {
        return reportCount;
    }

    public void setReportCount(int reportCount) {
        this.reportCount = reportCount;
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

    public int getUserNo() {
        return userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    @Override
    public String toString() {
        return "ReviewInfoDTO{" +
                "reviewNo=" + reviewNo +
                ", reviewContent='" + reviewContent + '\'' +
                ", reviewScope=" + reviewScope +
                ", reviewDate='" + reviewDate + '\'' +
                ", reviewImage='" + reviewImage + '\'' +
                ", reportCount=" + reportCount +
                ", storeNo=" + storeNo +
                ", storeName='" + storeName + '\'' +
                ", userNo=" + userNo +
                ", userName='" + userName + '\'' +
                ", userProfile='" + userProfile + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}

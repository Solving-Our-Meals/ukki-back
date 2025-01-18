package com.ohgiraffers.ukki.store.model.dto;

public class DetailReviewInfoDTO {

    private long storeNo;
    private long reviewNo;
    private String reviewContent;
    private String reviewImage;
    private String reviewDate;
    private int reviewScope;
    private long userNo;
    private String userName;
    private String userProfile;
    private long reportCount;
    private long resNo;

    public DetailReviewInfoDTO(){}

    public DetailReviewInfoDTO(long storeNo, long reviewNo, String reviewContent, String reviewImage, String reviewDate, int reviewScope, long userNo, String userName, String userProfile, long reportCount, long resNo) {
        this.storeNo = storeNo;
        this.reviewNo = reviewNo;
        this.reviewContent = reviewContent;
        this.reviewImage = reviewImage;
        this.reviewDate = reviewDate;
        this.reviewScope = reviewScope;
        this.userNo = userNo;
        this.userName = userName;
        this.userProfile = userProfile;
        this.reportCount = reportCount;
        this.resNo = resNo;
    }

    public long getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(long storeNo) {
        this.storeNo = storeNo;
    }

    public long getReviewNo() {
        return reviewNo;
    }

    public void setReviewNo(long reviewNo) {
        this.reviewNo = reviewNo;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public String getReviewImage() {
        return reviewImage;
    }

    public void setReviewImage(String reviewImage) {
        this.reviewImage = reviewImage;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public int getReviewScope() {
        return reviewScope;
    }

    public void setReviewScope(int reviewScope) {
        this.reviewScope = reviewScope;
    }

    public long getUserNo() {
        return userNo;
    }

    public void setUserNo(long userNo) {
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

    public long getReportCount() {
        return reportCount;
    }

    public void setReportCount(long reportCount) {
        this.reportCount = reportCount;
    }

    public long getResNo() {
        return resNo;
    }

    public void setResNo(long resNo) {
        this.resNo = resNo;
    }

    @Override
    public String toString() {
        return "DetailReviewInfoDTO{" +
                "storeNo=" + storeNo +
                ", reviewNo=" + reviewNo +
                ", reviewContent='" + reviewContent + '\'' +
                ", reviewImg='" + reviewImage + '\'' +
                ", reviewDate='" + reviewDate + '\'' +
                ", reviewScope=" + reviewScope +
                ", userNo=" + userNo +
                ", userName='" + userName + '\'' +
                ", userProfile='" + userProfile + '\'' +
                ", reportCount=" + reportCount +
                ", resNo=" + resNo +
                '}';
    }
}

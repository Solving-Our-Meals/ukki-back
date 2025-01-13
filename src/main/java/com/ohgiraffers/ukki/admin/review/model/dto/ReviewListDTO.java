package com.ohgiraffers.ukki.admin.review.model.dto;

public class ReviewListDTO {

    private int reviewNo;
    private String reviewContent;
    private int reviewScope;
    private String reviewDate;
    private String reviewStoreName;
    private String reviewUserId;

    public ReviewListDTO(){}

    public ReviewListDTO(int reviewNo, String reviewContent, int reviewScope, String reviewDate, String reviewStoreName, String reviewUserId) {
        this.reviewNo = reviewNo;
        this.reviewContent = reviewContent;
        this.reviewScope = reviewScope;
        this.reviewDate = reviewDate;
        this.reviewStoreName = reviewStoreName;
        this.reviewUserId = reviewUserId;
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

    public String getReviewStoreName() {
        return reviewStoreName;
    }

    public void setReviewStoreName(String reviewStoreName) {
        this.reviewStoreName = reviewStoreName;
    }

    public String getReviewUserId() {
        return reviewUserId;
    }

    public void setReviewUserId(String reviewUserId) {
        this.reviewUserId = reviewUserId;
    }

    @Override
    public String toString() {
        return "ReviewListDTO{" +
                "reviewNo=" + reviewNo +
                ", reviewContent='" + reviewContent + '\'' +
                ", reviewScope=" + reviewScope +
                ", reviewDate='" + reviewDate + '\'' +
                ", reviewStoreName='" + reviewStoreName + '\'' +
                ", reviewUserId='" + reviewUserId + '\'' +
                '}';
    }
}

//package com.ohgiraffers.ukki.store.model.dto;
//
//public class ReviewDTO {
//
//    private long reviewNo;
//    private long storeNo;
//    private long userNo;
//    private String userName;
//    private String userProfile;
//    private String reviewContent;
//    private String reviewDate;
//    private String reviewImage;
//    private long reportCount;
//    private long reviewCount;
//
//    public ReviewDTO(){};
//
//    public ReviewDTO(long reviewNo, long storeNo, long userNo, String userName, String userProfile, String reviewContent, String reviewDate, String reviewImage, long reportCount, long reviewCount) {
//        this.reviewNo = reviewNo;
//        this.storeNo = storeNo;
//        this.userNo = userNo;
//        this.userName = userName;
//        this.userProfile = userProfile;
//        this.reviewContent = reviewContent;
//        this.reviewDate = reviewDate;
//        this.reviewImage = reviewImage;
//        this.reportCount = reportCount;
//        this.reviewCount = reviewCount;
//    }
//
//    public long getReviewNo() {
//        return reviewNo;
//    }
//
//    public void setReviewNo(long reviewNo) {
//        this.reviewNo = reviewNo;
//    }
//
//    public long getStoreNo() {
//        return storeNo;
//    }
//
//    public void setStoreNo(long storeNo) {
//        this.storeNo = storeNo;
//    }
//
//    public long getUserNo() {
//        return userNo;
//    }
//
//    public void setUserNo(long userNo) {
//        this.userNo = userNo;
//    }
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
//
//    public String getUserProfile() {
//        return userProfile;
//    }
//
//    public void setUserProfile(String userProfile) {
//        this.userProfile = userProfile;
//    }
//
//    public String getReviewContent() {
//        return reviewContent;
//    }
//
//    public void setReviewContent(String reviewContent) {
//        this.reviewContent = reviewContent;
//    }
//
//    public String getReviewDate() {
//        return reviewDate;
//    }
//
//    public void setReviewDate(String reviewDate) {
//        this.reviewDate = reviewDate;
//    }
//
//    public String getReviewImage() {
//        return reviewImage;
//    }
//
//    public void setReviewImage(String reviewImage) {
//        this.reviewImage = reviewImage;
//    }
//
//    public long getReportCount() {
//        return reportCount;
//    }
//
//    public void setReportCount(long reportCount) {
//        this.reportCount = reportCount;
//    }
//
//    public long getReviewCount() {
//        return reviewCount;
//    }
//
//    public void setReviewCount(long reviewCount) {
//        this.reviewCount = reviewCount;
//    }
//
//    @Override
//    public String toString() {
//        return "ReviewDTO{" +
//                "reviewNo=" + reviewNo +
//                ", storeNo=" + storeNo +
//                ", userNo=" + userNo +
//                ", userName='" + userName + '\'' +
//                ", userProfile='" + userProfile + '\'' +
//                ", reviewContent='" + reviewContent + '\'' +
//                ", reviewDate='" + reviewDate + '\'' +
//                ", reviewImage='" + reviewImage + '\'' +
//                ", reportCount=" + reportCount +
//                ", reviewCount=" + reviewCount +
//                '}';
//    }
//}

package com.ohgiraffers.ukki.store.model.dto;

import java.util.List;

public class ReviewDTO {

    private long storeNo;
    private long reviewCount;
    private List<ReviewContentDTO> reviewList;

    public ReviewDTO(){};

    public ReviewDTO(long storeNo, long reviewCount, List<ReviewContentDTO> reviewList) {
        this.storeNo = storeNo;
        this.reviewCount = reviewCount;
        this.reviewList = reviewList;
    }

    public long getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(long storeNo) {
        this.storeNo = storeNo;
    }

    public long getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(long reviewCount) {
        this.reviewCount = reviewCount;
    }

    public List<ReviewContentDTO> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<ReviewContentDTO> reviewList) {
        this.reviewList = reviewList;
    }

    @Override
    public String toString() {
        return "ReviewDTO{" +
                "storeNo=" + storeNo +
                ", reviewCount=" + reviewCount +
                ", reviewList=" + reviewList +
                '}';
    }
}

package com.ohgiraffers.ukki.admin.user.model.dto;

public class AdminUserActInfoDTO {

    private int userNo;
    private int resCount;
    private int reviewCount;
    private int randomCount;

    public AdminUserActInfoDTO() {}

    public AdminUserActInfoDTO(int userNo, int resCount, int reviewCount, int randomCount) {
        this.userNo = userNo;
        this.resCount = resCount;
        this.reviewCount = reviewCount;
        this.randomCount = randomCount;
    }

    public int getUserNo() {
        return userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }

    public int getResCount() {
        return resCount;
    }

    public void setResCount(int resCount) {
        this.resCount = resCount;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public int getRandomCount() {
        return randomCount;
    }

    public void setRandomCount(int randomCount) {
        this.randomCount = randomCount;
    }

    @Override
    public String toString() {
        return "AdminUserActInfoDTO{" +
                "userNo=" + userNo +
                ", resCount=" + resCount +
                ", reviewCount=" + reviewCount +
                ", randomCount=" + randomCount +
                '}';
    }
}

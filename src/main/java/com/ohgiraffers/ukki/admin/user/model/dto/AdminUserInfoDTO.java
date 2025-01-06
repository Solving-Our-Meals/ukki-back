package com.ohgiraffers.ukki.admin.user.model.dto;

import com.ohgiraffers.ukki.common.UserRole;

public class AdminUserInfoDTO {

    private int userNo;
    private String userId;
    private String email;
    private String userName;
    private String userProfile;
    private UserRole userRole;
    private int noShow;
    private int resCount;
    private int reviewCount;
    private int randomCount;

    public AdminUserInfoDTO(){}

    public AdminUserInfoDTO(int userNo, String userId, String email, String userName, String userProfile, UserRole userRole, int noShow, int resCount, int reviewCount, int randomCount) {
        this.userNo = userNo;
        this.userId = userId;
        this.email = email;
        this.userName = userName;
        this.userProfile = userProfile;
        this.userRole = userRole;
        this.noShow = noShow;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public int getNoShow() {
        return noShow;
    }

    public void setNoShow(int noShow) {
        this.noShow = noShow;
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
        return "AdminUserInfoDTO{" +
                "userNo=" + userNo +
                ", userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", userProfile='" + userProfile + '\'' +
                ", userRole=" + userRole +
                ", noShow=" + noShow +
                ", resCount=" + resCount +
                ", reviewCount=" + reviewCount +
                ", randomCount=" + randomCount +
                '}';
    }
}

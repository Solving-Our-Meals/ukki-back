package com.ohgiraffers.ukki.admin.user.model.dto;

import com.ohgiraffers.ukki.common.UserRole;

public class AdminUserDTO {

    private int userNo;
    private String userId;
    private String email;
    private String userName;
    private String userProfile;
    private UserRole userRole;
    private int noShow;

    public AdminUserDTO() {}

    public AdminUserDTO(int userNo, String userId, String email, String userName, String userProfile, UserRole userRole, int noShow) {
        this.userNo = userNo;
        this.userId = userId;
        this.email = email;
        this.userName = userName;
        this.userProfile = userProfile;
        this.userRole = userRole;
        this.noShow = noShow;
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

    @Override
    public String toString() {
        return "AdminUserDTO{" +
                "userNo=" + userNo +
                ", userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", userProfile='" + userProfile + '\'' +
                ", userRole=" + userRole +
                ", noShow=" + noShow +
                '}';
    }
}

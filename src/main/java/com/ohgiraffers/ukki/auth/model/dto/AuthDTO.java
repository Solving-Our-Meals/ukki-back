package com.ohgiraffers.ukki.auth.model.dto;

import com.ohgiraffers.ukki.common.UserRole;

public class AuthDTO {
    private String userNo;
    private String userId;
    private String userPass;
    private String email;
    private UserRole userRole;
    private boolean isActive = true;
    private boolean noshow;

    public AuthDTO() {}

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isNoshow() {
        return noshow;
    }

    public void setNoshow(boolean noshow) {
        this.noshow = noshow;
    }

    @Override
    public String toString() {
        return "AuthDTO{" +
                "userNo='" + userNo + '\'' +
                ", userId='" + userId + '\'' +
                ", userPass='" + userPass + '\'' +
                ", email='" + email + '\'' +
                ", userRole=" + userRole +
                ", isActive=" + isActive +
                ", noshow=" + noshow +
                '}';
    }
}

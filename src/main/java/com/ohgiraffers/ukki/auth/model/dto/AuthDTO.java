package com.ohgiraffers.ukki.auth.model.dto;

import com.ohgiraffers.ukki.common.UserRole;

public class AuthDTO {
    private String userId;
    private String userPass;
    private String email;
    private UserRole userRole;
    private boolean isActive;


    public AuthDTO() {}

    public AuthDTO(String userId, String userPass, String email, UserRole userRole, boolean isActive) {
        this.userId = userId;
        this.userPass = userPass;
        this.email = email;
        this.userRole = userRole;
        this.isActive = isActive;
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

    @Override
    public String toString() {
        return "AuthDTO{" +
                "userId='" + userId + '\'' +
                ", userPass='" + userPass + '\'' +
                ", email='" + email + '\'' +
                ", userRole=" + userRole +
                ", isActive=" + isActive +
                '}';
    }
}

package com.ohgiraffers.ukki.admin.store.model.dto;

public class AdminStoreUserDTO {
    private String userId;
    private String userPassword;
    private String email;
    private String userName;

    public AdminStoreUserDTO(){}

    public AdminStoreUserDTO(String userId, String userPassword, String email, String userName) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.email = email;
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
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

    @Override
    public String toString() {
        return "AdminStoreUserDTO{" +
                "userId='" + userId + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}

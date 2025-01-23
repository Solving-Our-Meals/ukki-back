package com.ohgiraffers.ukki.user.model.dto;

public class MypageInfoDTO {
    private String userId;
    private String userName;
    private String Email;

    public MypageInfoDTO() {}

    public MypageInfoDTO(String userId, String userName, String email) {
        this.userId = userId;
        this.userName = userName;
        Email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    @Override
    public String toString() {
        return "MypageInfoDTO{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", Email='" + Email + '\'' +
                '}';
    }
}

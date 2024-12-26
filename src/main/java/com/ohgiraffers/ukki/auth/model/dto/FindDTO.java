package com.ohgiraffers.ukki.auth.model.dto;

public class FindDTO {
    private String email;
    private String type;
    private String userId;
    private String userPass;

    public FindDTO() {}

    public FindDTO(String email, String type, String userId, String userPass) {
        this.email = email;
        this.type = type;
        this.userId = userId;
        this.userPass = userPass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    @Override
    public String toString() {
        return "FindDTO{" +
                "email='" + email + '\'' +
                ", type='" + type + '\'' +
                ", userId='" + userId + '\'' +
                ", userPass='" + userPass + '\'' +
                '}';
    }
}

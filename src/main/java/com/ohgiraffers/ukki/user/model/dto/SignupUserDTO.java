package com.ohgiraffers.ukki.user.model.dto;

public class SignupUserDTO {

    private String userId;
    private String userPass;
    private String email;
    private String userName;
    private int noshow;

    public SignupUserDTO() {}

    public SignupUserDTO(String userId, String userPass, String email, String userName, int noshow) {
        this.userId = userId;
        this.userPass = userPass;
        this.email = email;
        this.userName = userName;
        this.noshow = noshow;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getNoshow() {
        return noshow;
    }

    public void setNoshow(int noshow) {
        this.noshow = noshow;
    }

    @Override
    public String toString() {
        return "SignupUserDTO{" +
                "userId='" + userId + '\'' +
                ", userPass='" + userPass + '\'' +
                ", email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", noshow=" + noshow +
                '}';
    }
}

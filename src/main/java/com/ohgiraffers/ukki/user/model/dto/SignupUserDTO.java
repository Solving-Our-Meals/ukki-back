package com.ohgiraffers.ukki.user.model.dto;

public class SignupUserDTO {

    private String userName;
    private String userPass;
    private String email;
    private String userNick;

    public SignupUserDTO() {}

    public SignupUserDTO(String userName, String userPass, String email, String userNick) {
        this.userName = userName;
        this.userPass = userPass;
        this.email = email;
        this.userNick = userNick;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }
}

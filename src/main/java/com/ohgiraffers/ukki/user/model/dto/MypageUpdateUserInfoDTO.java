package com.ohgiraffers.ukki.user.model.dto;

public class MypageUpdateUserInfoDTO {

    private String userId;
    private String userName;
    private String userPass;
    private String File;

    public MypageUpdateUserInfoDTO() {}

    public MypageUpdateUserInfoDTO(String userId, String userName, String userPass, String file) {
        this.userId = userId;
        this.userName = userName;
        this.userPass = userPass;
        File = file;
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

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getFile() {
        return File;
    }

    public void setFile(String file) {
        File = file;
    }

    @Override
    public String toString() {
        return "MypageUpdateUserInfoDTO{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userPass='" + userPass + '\'' +
                ", File='" + File + '\'' +
                '}';
    }
}

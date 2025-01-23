package com.ohgiraffers.ukki.user.model.dto;

public class MypageProfileImageDTO {
    private String userId;
    private String file;

    public MypageProfileImageDTO() {}

    public MypageProfileImageDTO(String userId, String file) {
        this.userId = userId;
        this.file = file;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "MypageProfileImageDTO{" +
                "userId='" + userId + '\'' +
                ", file='" + file + '\'' +
                '}';
    }
}

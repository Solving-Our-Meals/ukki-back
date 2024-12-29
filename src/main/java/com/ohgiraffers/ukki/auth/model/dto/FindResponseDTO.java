package com.ohgiraffers.ukki.auth.model.dto;

public class FindResponseDTO {
    private String userId;
    private boolean success;

    public FindResponseDTO() {}

    public FindResponseDTO(String userId, boolean success) {
        this.userId = userId;
        this.success = success;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isSuccess() {  // isSuccess로 수정
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "FindResponseDTO{" +
                "userId='" + userId + '\'' +
                ", success=" + success +
                '}';
    }
}

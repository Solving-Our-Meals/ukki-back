package com.ohgiraffers.ukki.auth.model.dto;

public class FindResponseDTO {
    private String message;
    private boolean success;
    private String userId;

    public FindResponseDTO() {}
    
    // 아디용
    public FindResponseDTO(String message, boolean success, String userId) {
        this.message = message;
        this.success = success;
        this.userId = userId;
    }

    // 비번용
    public FindResponseDTO(String message, boolean success) {
        this.message = message;
        this.success = success;
        this.userId = null;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "FindResponseDTO{" +
                "message='" + message + '\'' +
                ", success=" + success +
                ", userId='" + userId + '\'' +
                '}';
    }
}

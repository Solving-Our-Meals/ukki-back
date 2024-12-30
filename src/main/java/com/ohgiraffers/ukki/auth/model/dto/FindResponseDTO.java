package com.ohgiraffers.ukki.auth.model.dto;

public class FindResponseDTO {
    private String userId;
    private boolean success;
    private String message;

    public FindResponseDTO() {}

    public FindResponseDTO(String userId, boolean success, String message) {
        this.userId = userId;
        this.success = success;
        this.message = message;
    }

    public FindResponseDTO(String userId, boolean success) {
        this.userId = userId;
        this.success = success;
    }

    public FindResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "FindResponseDTO{" +
                "userId='" + userId + '\'' +
                ", success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}

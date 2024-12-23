package com.ohgiraffers.ukki.auth.model.dto;

public class JwtTokenDTO {
    private String message;
    private String token;

    public JwtTokenDTO() {}

    public JwtTokenDTO(String message, String token) {
        this.message = message;
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "JwtTokenDTO{" +
                "message='" + message + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
